package rpc

import protobuf.MatrixDouble.DMatrix
import protobuf.MatrixLong.Matrix

trait NettyClient {
  def sendModel(matrix: Matrix):Boolean
  def sendParameter(dMatrix: DMatrix): Boolean
}
