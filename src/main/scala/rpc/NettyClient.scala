package rpc

import protobuf.MatrixDouble.DMatrix
import protobuf.MatrixLong.Matrix

trait NettyClient {
  def sendModel(matrix: Matrix , ip :String , port : Int):Boolean
  def sendParameter(dMatrix: DMatrix, ip: String, port : Int): Boolean
}
