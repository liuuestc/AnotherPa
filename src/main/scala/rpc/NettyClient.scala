package rpc

trait NettyClient {
  def getPort():String
  def send():Boolean
}
