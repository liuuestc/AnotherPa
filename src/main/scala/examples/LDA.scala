package examples

import java.util.UUID

import client.Client
import conf.APSConfiguration
import io.BlockMangerImpl
import protobuf.MatrixLong.{Matrix, Row}

import scala.collection.mutable.ListBuffer
import scala.util.Random

class LDA extends Client{

  def initial(conf: APSConfiguration): Unit ={
    conf.setAppName("LDA")
    conf.setDataPath("")
    conf.set("LDAClasspath",this.getClass.getName)
    conf.setJarPath("Hello World")
  }

  //主题个数
  var k = 0
  //词袋大小
  var v = 0

  //先验
  var a = 1
  var b = 1
  // 主题计数矩阵
  val topicCountBuilder = Matrix.newBuilder()
  val topicRowBuilder = Row.newBuilder()
  for (i <- 0 until(k)) topicRowBuilder.addNum(0L)

  //词矩阵和临时结构
  val wordTopicRow = Row.newBuilder()
  val wordTopicBuilder = Matrix.newBuilder()
  val wordTopic = dim2L(v,k)

  //文章每个词对应的topic
  val docToTopicMatrix =new ListBuffer[Matrix]
  val docToTopicRow = Row.newBuilder()
  val docToTopicBuilder = Matrix.newBuilder()

  //文章的topic分布
  var totalDoc = 0
  val docTopic = dim2L(totalDoc,k)
  override def selectOne: String = ???

  override def isPs: Boolean = true



  override def inputPath: String ="outputPath"

  override def outputPath: String = ???

  override def compute(blockMangerImpl: BlockMangerImpl, id: Long): Boolean = {
      //获取需要计算的模型
      var model = new ListBuffer[Matrix]
      for (i <- 0 until blockMangerImpl.getModels.size()){
        if (blockMangerImpl.getModels.get(i).getId == id)
          model.append(blockMangerImpl.getModels.get(i))
      }
      val m = model(0)
      //获取训练的数据
      val trainData = blockMangerImpl.getTrainData
      //主题计数
      val topicCount = blockMangerImpl.getParameters.get(0)


      for ( i <- 0 until trainData.size()){
        val mtx = trainData.get(i)
        //文章词-主题矩阵
        val dmtx = docToTopicMatrix(i)
        for (j <- 0 until mtx.getRowCount){
          val doc = mtx.getRow(j)
          val tdoc = dmtx.getRow(j).newBuilderForType()
          for (k <- 0 until doc.getNumCount){
            val t = tdoc.getNum(k)
            val w = doc.getNum(k)
            //计算后w新分配的主题。

            val tt = astopic()
            if(tt != t.asInstanceOf[Int]){
              //更新主题
              tdoc.setNum(k,tt)
              val row = m.getRow(w.asInstanceOf[Int])
              val  pre1 = row.getNum(t.asInstanceOf[Int])
              val post1 = row.getNum(tt)
              row.newBuilderForType().setNum(tt,post1+1).build()
              row.newBuilderForType().setNum(t.asInstanceOf[Int],pre1-1).build()
              docTopic.apply(i).apply(t.asInstanceOf[Int]) -=1
              docTopic.apply(i).apply(tt) += 1
              topicCount.getRow(0).newBuilderForType().setNum(t.asInstanceOf[Int],topicCount.getRow(0).getNum(t.asInstanceOf[Int])-1)
              topicCount.getRow(0).newBuilderForType().setNum(t.asInstanceOf[Int],topicCount.getRow(0).getNum(tt + 1))
            }
          }
        }
      }

    false
  }


  //server端merger频率计数
  override def merge(blockMangerImpl: BlockMangerImpl, id: Long): Boolean = {
    var tmp = 0
    if (blockMangerImpl.getParameters.size() == 0) false
    else {
      for (i <- 0 until blockMangerImpl.getParameters.size()) {
        if (id == blockMangerImpl.getParameters.get(i).getId) {
          blockMangerImpl.getParameters.get(i)
          tmp = i
        }
      }
      if (tmp == blockMangerImpl.getParameters.size()) false
      else {
        if (tmp == 0)
          true
        else {
          //获取原始的params
          val params = blockMangerImpl.getParameters.get(0)
          val tmpMatrix = blockMangerImpl.getParameters.get(tmp)
          for (ii <- 0 until params.getRowCount) {
            val row = params.getRow(ii).newBuilderForType()
            val tmprow = tmpMatrix.getRow(ii)
            for (j <- 0 until row.getNumCount) {
              row.setNum(j, row.getNum(j) + tmprow.getNum(j))
            }
            row.build()
          }
          true
        }
      }
    }
  }
  override def init(blockMangerImpl: BlockMangerImpl): Unit ={
    val trainData = blockMangerImpl.getTrainData
    val wordTopicId = UUID.randomUUID().getLeastSignificantBits
    val topicCountId = UUID.randomUUID().getLeastSignificantBits

    topicCountBuilder.setId(topicCountId)
    val size = trainData.size()
    var totalDoc = 0
    for (i <- 0 until(size)) {
      totalDoc += trainData.get(i).getRowCount
    }
    //文章长度
    val docSize = new ListBuffer[Int]
    this.totalDoc = totalDoc
   //训练数据的列表长度
    val random = new Random()
    for (i <- 0 until size){
      //获得一个训练数据矩阵
      val matrix = trainData.get(i)
      for (rowId <- 0 until matrix.getRowCount){
        //获得一篇文章
        val row = matrix.getRow(rowId)
        //设置每一篇文章的长度
        docSize.append(row.getNumCount)
       for (numId <- 0 until row.getNumCount){
         val word = row.getNum(numId)
         val topic = random.nextInt(k)
           val topicLong = topic.asInstanceOf[Long]
         //更新文章-主题矩阵，词-主题矩阵，文章词所属topic，主题计数矩阵
         docTopic.apply(rowId).apply(topic) +=1
         wordTopic.apply(word.asInstanceOf[Int]).apply(topic) +=1
         docToTopicRow.addNum(topicLong)
         topicRowBuilder.setNum(topic,topicRowBuilder.getNum(topic)+1)
       }
        docToTopicBuilder.addRow(docToTopicRow)
        docToTopicRow.clear()
      }
      docToTopicMatrix.append(docToTopicBuilder.build())
      docToTopicBuilder.clear()
    }
    for (topics <- wordTopic){
      wordTopicRow.setRowNum(topics.size)
      for(topic <- topics)
        wordTopicRow.addNum(topic)
      wordTopicBuilder.addRow(wordTopicRow)
      wordTopicRow.clear()
    }
    //将词-主题矩阵和主题计数矩阵返回
    blockMangerImpl.addModels(wordTopicBuilder.build())
    blockMangerImpl.addParamters(topicCountBuilder.setId(topicCountId).addRow(topicRowBuilder.setRowNum(topicCountId)).build())
  }

  //分配新的主题
  def astopic() :Int ={1}
  def dim2L(rows : Int, cols : Int):Array[Array[Long]]={
    val d2 :Array[Array[Long]] = new Array(rows)
    for (k <-0 until rows ) {
      d2(k) = new Array[Long](cols)
    }
    d2
  }
  def gibbsSample() = ???

}
