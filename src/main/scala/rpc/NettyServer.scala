package rpc

trait NettyServer {
  def getPort():String
  def send():Boolean
  def start():Boolean
}
