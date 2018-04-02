package conf;

import sun.tools.jar.resources.jar;
import util.TextMultiOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.OutputFormat;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import util.Utilities;

public class APSConfiguration extends YarnConfiguration {

  private static final String APS_DEFAULT_XML_FILE = "aps-default.xml";

  private static final String APS_SITE_XML_FILE = "aps-site.xml";

  static {
    YarnConfiguration.addDefaultResource(APS_DEFAULT_XML_FILE);
    YarnConfiguration.addDefaultResource(APS_SITE_XML_FILE);
  }

  public APSConfiguration() {
    super();
  }

  public APSConfiguration(Configuration conf) {
    super(conf);
  }

  /**
   * Configuration used in Client
   */
  public static final String DEFAULT_APS_APP_TYPE = "APS";

  public static final String APS_WORKING_DIR = "aps.staging.dir";

  public static final String DEFAULT_APS_WORKING_DIR = "/tmp/APS/staging";

  public static final String APS_LOG_PULL_INTERVAL = "aps.log.pull.interval";

  public static final int DEFAULT_APS_LOG_PULL_INTERVAL = 10000;

  public static final String APS_USER_CLASSPATH_FIRST = "aps.user.classpath.first";

  public static final boolean DEFAULT_APS_USER_CLASSPATH_FIRST = true;

  public static final String APS_REPORT_CONTAINER_STATUS = "aps.report.container.status";

  public static final boolean DEFAULT_APS_REPORT_CONTAINER_STATUS = true;

  public static final String APS_AM_MEMORY = "aps.am.memory";

  public static final int DEFAULT_APS_AM_MEMORY = 1024;

  public static final String APS_AM_CORES = "aps.am.cores";

  public static final int DEFAULT_APS_AM_CORES = 1;

  public static final String APS_WORKER_MEMORY = "aps.worker.memory";

  public static final int DEFAULT_APS_WORKER_MEMORY = 1024;

  public static final String APS_WORKER_VCORES = "aps.worker.cores";

  public static final int DEFAULT_APS_WORKER_VCORES = 1;

  public static final String APS_WORKER_NUM = "aps.worker.num";

  public static final int DEFAULT_APS_WORKER_NUM = 1;

  /**
   * Get a time parameter as seconds; throws a NoSuchElementException if it's not set. If no
   * suffix is provided then seconds are assumed.
   * @throws
   */
  public Long getTimeAsSeconds(String key) {
    return  Utilities.timeStringAsSec(get(key));
  }
   public Long getTimeAsSeconds(String key, String defaultValue) {
       return getTimeAsSeconds(get(key, defaultValue));
  }

  //可能没用

  public static final String APS_PS_MEMORY = "aps.ps.memory";

  public static final int DEFAULT_APS_PS_MEMORY = 1024;

  public static final String APS_PS_VCORES = "aps.ps.cores";

  public static final int DEFAULT_APS_PS_VCORES = 1;

  public static final String APS_PS_NUM = "aps.ps.num";

  public static final int DEFAULT_APS_PS_NUM = 0;

  public static final String APS_WORKER_MEM_AUTO_SCALE = "aps.worker.mem.autoscale";

  public static final Double DEFAULT_APS_WORKER_MEM_AUTO_SCALE = 0.5;

  public static final String APS_PS_MEM_AUTO_SCALE = "aps.ps.mem.autoscale";

  public static final Double DEFAULT_APS_PS_MEM_AUTO_SCALE = 0.2;
  //

  public static final String APS_APP_MAX_ATTEMPTS = "aps.app.max.attempts";

  public static final int DEFAULT_APS_APP_MAX_ATTEMPTS = 1;

  public static final String APS_APP_QUEUE = "aps.app.queue";

  public static final String DEFAULT_APS_APP_QUEUE = "DEFAULT";

  public static final String APS_APP_PRIORITY = "aps.app.priority";

  public static final int DEFAULT_APS_APP_PRIORITY = 3;

  public static final String APS_OUTPUT_LOCAL_DIR = "aps.output.local.dir";

  public static final String DEFAULT_APS_OUTPUT_LOCAL_DIR = "output";

  public static final String APS_INPUTF0RMAT_CLASS = "aps.inputformat.class";

  public static final Class<? extends InputFormat> DEFAULT_APS_INPUTF0RMAT_CLASS = org.apache.hadoop.mapred.TextInputFormat.class;

  public static final String APS_OUTPUTFORMAT_CLASS = "aps.outputformat.class";

  public static final Class<? extends OutputFormat> DEFAULT_APS_OUTPUTF0RMAT_CLASS = TextMultiOutputFormat.class;

  public static final String APS_INPUTFILE_RENAME = "aps.inputfile.rename";

  public static final Boolean DEFAULT_APS_INPUTFILE_RENAME = false;

  public static final String APS_INPUT_STRATEGY = "aps.input.strategy";

  public static final String DEFAULT_APS_INPUT_STRATEGY = "DOWNLOAD";

  public static final String APS_OUTPUT_STRATEGY = "aps.output.strategy";

  public static final String DEFAULT_APS_OUTPUT_STRATEGY = "UPLOAD";

  public static final String APS_INPUTFORMAT_CACHESIZE_LIMIT= "aps.inputformat.cachesize.limit";

  public static final int DEFAULT_APS_INPUTFORMAT_CACHESIZE_LIMIT = 100 * 1024;

  public static final String APS_INPUTFORMAT_CACHE = "aps.inputformat.cache";

  public static final boolean DEFAULT_APS_INPUTFORMAT_CACHE = false;

  public static final String APS_INPUTFORMAT_CACHEFILE_NAME = "aps.inputformat.cachefile.name";

  public static final String DEFAULT_APS_INPUTFORMAT_CACHEFILE_NAME = "inputformatCache.gz";

  public static final String APS_INTERREAULST_DIR = "aps.interresult.dir";

  public static final String DEFAULT_APS_INTERRESULT_DIR = "/interResult_";

  public static final String[] DEFAULT_APS_APPLICATION_CLASSPATH = {
      "$HADOOP_CONF_DIR",
      "$HADOOP_COMMON_HOME/share/hadoop/common/*",
      "$HADOOP_COMMON_HOME/share/hadoop/common/lib/*",
      "$HADOOP_HDFS_HOME/share/hadoop/hdfs/*",
      "$HADOOP_HDFS_HOME/share/hadoop/hdfs/lib/*",
      "$HADOOP_YARN_HOME/share/hadoop/yarn/*",
      "$HADOOP_YARN_HOME/share/hadoop/yarn/lib/*",
      "$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*",
      "$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*"
  };

  /**
   * Configuration used in ApplicationMaster
   */
  public static final String APS_AKKASYSTEM_SEEDK_NODES_PORT = "aps.akkaSystemSeeds.port";

  public static final String APS_CONTAINER_EXTRA_JAVA_OPTS = "aps.container.extra.java.opts";

  public static final String DEFAULT_APS_CONTAINER_JAVA_OPTS_EXCEPT_MEMORY = "";

  public static final String APS_ALLOCATE_INTERVAL = "aps.allocate.interval";

  public static final int DEFAULT_APS_ALLOCATE_INTERVAL = 1000;

  public static final String APS_STATUS_UPDATE_INTERVAL = "aps.status.update.interval";

  public static final int DEFAULT_APS_STATUS_PULL_INTERVAL = 1000;

  public static final String APS_TASK_TIMEOUT = "aps.task.timeout";

  public static final int DEFAULT_APS_TASK_TIMEOUT = 5 * 60 * 1000;

  public static final String APS_LOCALRESOURCE_TIMEOUT = "aps.localresource.timeout";

  public static final int DEFAULT_APS_LOCALRESOURCE_TIMEOUT = 5 * 60 * 1000;

  public static final String APS_TASK_TIMEOUT_CHECK_INTERVAL_MS = "aps.task.timeout.check.interval";

  public static final int DEFAULT_APS_TASK_TIMEOUT_CHECK_INTERVAL_MS = 3 * 1000;

  public static final String APS_INTERRESULT_UPLOAD_TIMEOUT = "aps.interresult.upload.timeout";

  public static final int DEFAULT_APS_INTERRESULT_UPLOAD_TIMEOUT = 50 * 60 * 1000;

  public static final String APS_MESSAGES_LEN_MAX = "aps.messages.len.max";

  public static final int DEFAULT_APS_MESSAGES_LEN_MAX = 1000;

  public static final String APS_EXECUTE_NODE_LIMIT = "aps.execute.node.limit";

  public static final int DEFAULT_APS_EXECUTENODE_LIMIT = 200;

  public static final String APS_CLEANUP_ENABLE = "aps.cleanup.enable";

  public static final boolean DEFAULT_APS_CLEANUP_ENABLE = true;

  public static final String APS_CONTAINER_MAX_FAILURES_RATE = "aps.container.maxFailures.rate";

  public static final double DEFAULT_APS_CONTAINER_FAILURES_RATE = 0.5;

  /**
   * Configuration used in Container
   */
  public static final String APS_DOWNLOAD_FILE_RETRY = "aps.download.file.retry";

  public static final int DEFAULT_APS_DOWNLOAD_FILE_RETRY = 3;

  public static final String APS_DOWNLOAD_FILE_THREAD_NUMS = "aps.download.file.thread.nums";

  public static final int DEFAULT_APS_DOWNLOAD_FILE_THREAD_NUMS = 10;

  public static final String APS_CONTAINER_HEARTBEAT_INTERVAL = "aps.container.heartbeat.interval";

  public static final int DEFAULT_APS_CONTAINER_HEARTBEAT_INTERVAL = 10 * 1000;

  public static final String APS_CONTAINER_HEARTBEAT_RETRY = "aps.container.heartbeat.retry";

  public static final int DEFAULT_APS_CONTAINER_HEARTBEAT_RETRY = 3;

  public static final String APS_CONTAINER_UPDATE_APP_STATUS_INTERVAL = "aps.container.update.appstatus.interval";

  public static final int DEFAULT_APS_CONTAINER_UPDATE_APP_STATUS_INTERVAL = 3 * 1000;

  public static final String APS_CONTAINER_AUTO_CREATE_OUTPUT_DIR = "aps.container.auto.create.output.dir";

  public static final boolean DEFAULT_APS_CONTAINER_AUTO_CREATE_OUTPUT_DIR = true;

  /**
   * Configuration used in Log Dir
   */
  public static final String APS_HISTORY_LOG_DIR = "aps.history.log.dir";

  public static final String DEFAULT_APS_HISTORY_LOG_DIR = "/tmp/APS/history";

  public static final String APS_HISTORY_LOG_DELETE_MONITOR_TIME_INTERVAL = "aps.history.log.delete-monitor-time-interval";

  public static final int DEFAULT_APS_HISTORY_LOG_DELETE_MONITOR_TIME_INTERVAL = 24 * 60 * 60 * 1000;

  public static final String APS_HISTORY_LOG_MAX_AGE_MS = "aps.history.log.max-age-ms";

  public static final int DEFAULT_APS_HISTORY_LOG_MAX_AGE_MS = 24 * 60 * 60 * 1000;

  public APSConfiguration setAppName(String name) {
        set("spark.app.name", name);
        return this;
    }
    public APSConfiguration setJars(String[] jars){
      String result = "";
      for (String jar : jars){
          if(jar == null)
              System.out.println("null jar passed to SparkContext constructor");
          else
              result += jar + ",";
      }
      result.substring(0,result.length()-1);
        set("apa.jars",result);
        return this;
    }
}
