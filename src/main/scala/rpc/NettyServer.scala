package rpc

trait NettyServer {
  def getPort():String
  def start():Boolean
}
