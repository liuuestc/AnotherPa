package common

class ScheduleMaster(clientName : String) {
     def start() = {}
}
object ScheduleMaster{
  def apply(clientName: String): ScheduleMaster = new ScheduleMaster(clientName)
}
