package client

import protobuf.MatrixLong.Matrix

//调度器负责一部分参数的训练
trait Client  {
  def compute(matrix: Matrix):Matrix                 //参数在每台机器workers上的计算函数
                              //将数据加载到workers上，实现这个代码
  def inputPath : String
  //将每台机器的模型加载到outputPath下到目录中
  def outputPath : String
  def workerPs(matrix: Matrix):Matrix = ???                   //服务端对上传的参数的操作并返回所要上传的参数
  def getPs(matrix: Matrix)= ???                  //slaves 下载到参数后的操作
  def isPs : Boolean
  def init(matrix: Matrix) : Matrix                   //初始化操作，首先使用初始化操作
  def selectOne : String         //给出默认实现，选择参数要传到那个机器
  def merge (matrix: Matrix) : Matrix                     //服务端的收到传来的参数后的操作
}
