package Client

//服务端只用作调度
trait Client {
      def computer
      val inputPath : String
      val outputPath : String
      def init
      //给出默认实现，选择参数要传到那个机器
      def selectOne : String
}
