package client

import conf.APSConfiguration
import io.BlockMangerImpl
import protobuf.MatrixLong.Matrix

//调度器负责一部分参数的训练
trait Client  {
  // 将数据加载到workers上，实现这个代码
  def inputPath : String
  //将每台机器的模型加载到outputPath下到目录中
  def outputPath : String
  //参数在每台机器workers上的计算函数 ,计算后将数据存到BlockManagerImpl结构中相应的位置,id为model的id
  def compute(blockMangerImpl: BlockMangerImpl,id:Long):Boolean
  //服务端的收到传来的参数后的操作,id为参数矩阵到id
  def merge (blockMangerImpl: BlockMangerImpl,id : Long) : Matrix
  //用于设置APS的模式
  def isPs : Boolean
  //初始化操作，首先使用初始化操作
  def init(blockMangerImpl: BlockMangerImpl)
  //给出默认实现，选择参数要传到那个机器 ，服务器使用，根据用户编写的函数确定训练好的model要传输的下一个worker
  def selectOne : String
  //用户初始化配置文件
  def initial(conf: APSConfiguration)
}
