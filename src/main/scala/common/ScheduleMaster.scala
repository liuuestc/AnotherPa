package common

class ScheduleMaster(clientName : String, dataPath : String, numberOfContainer : Int) {
     def start() = {}
}
object ScheduleMaster{
  def apply(clientName: String, dataPath: String, numberOfContainer: Int): ScheduleMaster = new ScheduleMaster(clientName, dataPath, numberOfContainer)
}
