package common

import conf.APSConfiguration

class ScheduleMaster(clientName : String, dataPath : String, numberOfContainer : Int, host : String, conf : APSConfiguration) {
     def start() = {}
}
object ScheduleMaster{
  def apply(clientName: String, dataPath: String, numberOfContainer: Int, host: String, conf: APSConfiguration): ScheduleMaster = new ScheduleMaster(clientName, dataPath, numberOfContainer, host, conf)
}
