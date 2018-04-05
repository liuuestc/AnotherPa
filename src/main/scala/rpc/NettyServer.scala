package rpc

import protobuf.MatrixLong.Matrix

trait NettyServer {
  def initial(): Int
  def getMatrix() : Matrix
}
