package client

//调度器负责一部分参数的训练
trait APSClients extends Client{
  def compute                 //参数在每台机器workers上的计算函数
  //将数据加载到workers上，实现这个代码
  def loadDate(inputPath: String) = {}
  //将每台机器的模型加载到outputPath下到目录中
  def saveModel(outputPath : String) = {}
  def putPs                   //服务端对上传的参数的操作
  def getPs                   //slaves 下载到参数后的操作
  def init                    //初始化操作，首先使用初始化操作
  //给出默认实现，选择参数要传到那个机器
  def selectOne : String
}
