package util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import conf.APSConfiguration;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utilities {
  private static Log LOG = LogFactory.getLog(Utilities.class);

  private Utilities() {
  }


  public static String getLocalHost(){
        return NetUtils.getHostname().split("/")[1];
  }

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      LOG.warn("Sleeping are Interrupted ...", e);
    }
  }

  public static List<FileStatus> listStatusRecursively(Path path, FileSystem fs, List<FileStatus> fileStatuses)
      throws IOException {
    if (fileStatuses == null) {
      fileStatuses = new ArrayList<FileStatus>(1000);
    }
    LOG.info("input path: " + path.toString());
    FileStatus[] fileStatus = fs.listStatus(path);
    if (fileStatus != null && fileStatus.length > 0) {
      for (FileStatus f : fileStatus) {
        if (fs.isDirectory(f.getPath())) {
          listStatusRecursively(f.getPath(), fs, fileStatuses);
        } else {
          fileStatuses.add(f);
        }
      }
    } else {
      LOG.info("input list size:" + fileStatus.length);
      if (fileStatus == null) {
        LOG.info("fileStatus is null");
      }
    }
    return fileStatuses;
  }

  public static List<Path> convertStatusToPath(List<FileStatus> fileStatuses) {
    List<Path> paths = new ArrayList<Path>();
    if (fileStatuses != null) {
      for (FileStatus fileStatus : fileStatuses) {
        paths.add(fileStatus.getPath());
      }
    }
    return paths;
  }

  public static Path getRemotePath(APSConfiguration conf, ApplicationId appId, String fileName) {
    String pathSuffix = appId.toString() + "/" + fileName;
    Path remotePath = new Path(conf.get(APSConfiguration.APS_WORKING_DIR, APSConfiguration.DEFAULT_APS_WORKING_DIR), pathSuffix);
    remotePath = new Path(conf.get("fs.defaultFS"), remotePath);
    LOG.debug("Got remote path of " + fileName + " is " + remotePath.toString());
    return remotePath;
  }

  public static void setPathExecutableRecursively(String path) {
    File file = new File(path);
    if (!file.exists()) {
      LOG.warn("Path " + path + " does not exist!");
      return;
    }
    if (!file.setExecutable(true)) {
      LOG.error("Failed to set executable for " + path);
    }

    if (file.isDirectory()) {
      File[] files = file.listFiles();
      if (null != files && files.length > 0) {
        setPathExecutableRecursively(file.getAbsolutePath());
      }
    }
  }

  public static boolean mkdirs(String path) {
    return mkdirs(path, false);
  }

  public static boolean mkdirs(String path, boolean needDelete) {
    File file = new File(path);
    if (file.exists()) {
      if (needDelete) {
        file.delete();
      } else {
        return true;
      }
    }
    return file.mkdirs();
  }

  public static boolean mkParentDirs(String outFile) {
    File dir = new File(outFile);
    dir = dir.getParentFile();
    return dir.exists() || dir.mkdirs();
  }

  public static LocalResource createApplicationResource(FileSystem fs, Path path, LocalResourceType type)
      throws IOException {
    LocalResource localResource = Records.newRecord(LocalResource.class);
    FileStatus fileStatus = fs.getFileStatus(path);
    localResource.setResource(ConverterUtils.getYarnUrlFromPath(path));
    localResource.setSize(fileStatus.getLen());
    localResource.setTimestamp(fileStatus.getModificationTime());
    localResource.setType(type);
    localResource.setVisibility(LocalResourceVisibility.APPLICATION);
    return localResource;
  }

    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    /**
     * Define a default value for driver memory here since this value is referenced across the code
     * base and nearly all files already use Utils.scala
     */
    public static final long DEFAULT_DRIVER_MEM_MB = 1024;

    /** Closes the given object, ignoring IOExceptions. */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            logger.error("IOException should not have been thrown.", e);
        }
    }

    /** Returns a hash consistent with Spark's Utils.nonNegativeHash(). */
    public static int nonNegativeHash(Object obj) {
        if (obj == null) { return 0; }
        int hash = obj.hashCode();
        return hash != Integer.MIN_VALUE ? Math.abs(hash) : 0;
    }

    /**
     * Convert the given string to a byte buffer. The resulting buffer can be
     * converted back to the same string through {@link #bytesToString(ByteBuffer)}.
     */
    public static ByteBuffer stringToBytes(String s) {
        return Unpooled.wrappedBuffer(s.getBytes(StandardCharsets.UTF_8)).nioBuffer();
    }

    /**
     * Convert the given byte buffer to a string. The resulting string can be
     * converted back to the same byte buffer through {@link #stringToBytes(String)}.
     */
    public static String bytesToString(ByteBuffer b) {
        return Unpooled.wrappedBuffer(b).toString(StandardCharsets.UTF_8);
    }

    /**
     * Delete a file or directory and its contents recursively.
     * Don't follow directories if they are symlinks.
     *
     * @param file Input file / dir to be deleted
     * @throws IOException if deletion is unsuccessful
     */
    public static void deleteRecursively(File file) throws IOException {
        if (file == null) { return; }

        // On Unix systems, use operating system command to run faster
        // If that does not work out, fallback to the Java IO way
        if (SystemUtils.IS_OS_UNIX) {
            try {
                deleteRecursivelyUsingUnixNative(file);
                return;
            } catch (IOException e) {
                logger.warn("Attempt to delete using native Unix OS command failed for path = {}. " +
                        "Falling back to Java IO way", file.getAbsolutePath(), e);
            }
        }

        deleteRecursivelyUsingJavaIO(file);
    }

    private static void deleteRecursivelyUsingJavaIO(File file) throws IOException {
        if (file.isDirectory() && !isSymlink(file)) {
            IOException savedIOException = null;
            for (File child : listFilesSafely(file)) {
                try {
                    deleteRecursively(child);
                } catch (IOException e) {
                    // In case of multiple exceptions, only last one will be thrown
                    savedIOException = e;
                }
            }
            if (savedIOException != null) {
                throw savedIOException;
            }
        }

        boolean deleted = file.delete();
        // Delete can also fail if the file simply did not exist.
        if (!deleted && file.exists()) {
            throw new IOException("Failed to delete: " + file.getAbsolutePath());
        }
    }

    private static void deleteRecursivelyUsingUnixNative(File file) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("rm", "-rf", file.getAbsolutePath());
        Process process = null;
        int exitCode = -1;

        try {
            // In order to avoid deadlocks, consume the stdout (and stderr) of the process
            builder.redirectErrorStream(true);
            builder.redirectOutput(new File("/dev/null"));

            process = builder.start();

            exitCode = process.waitFor();
        } catch (Exception e) {
            throw new IOException("Failed to delete: " + file.getAbsolutePath(), e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }

        if (exitCode != 0 || file.exists()) {
            throw new IOException("Failed to delete: " + file.getAbsolutePath());
        }
    }

    private static File[] listFilesSafely(File file) throws IOException {
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files == null) {
                throw new IOException("Failed to list files for dir: " + file);
            }
            return files;
        } else {
            return new File[0];
        }
    }

    private static boolean isSymlink(File file) throws IOException {
        Preconditions.checkNotNull(file);
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            fileInCanonicalDir = new File(file.getParentFile().getCanonicalFile(), file.getName());
        }
        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }

    private static final ImmutableMap<String, TimeUnit> timeSuffixes =
            ImmutableMap.<String, TimeUnit>builder()
                    .put("us", TimeUnit.MICROSECONDS)
                    .put("ms", TimeUnit.MILLISECONDS)
                    .put("s", TimeUnit.SECONDS)
                    .put("m", TimeUnit.MINUTES)
                    .put("min", TimeUnit.MINUTES)
                    .put("h", TimeUnit.HOURS)
                    .put("d", TimeUnit.DAYS)
                    .build();

    private static final ImmutableMap<String, ByteUnit> byteSuffixes =
            ImmutableMap.<String, ByteUnit>builder()
                    .put("b", ByteUnit.BYTE)
                    .put("k", ByteUnit.KiB)
                    .put("kb", ByteUnit.KiB)
                    .put("m", ByteUnit.MiB)
                    .put("mb", ByteUnit.MiB)
                    .put("g", ByteUnit.GiB)
                    .put("gb", ByteUnit.GiB)
                    .put("t", ByteUnit.TiB)
                    .put("tb", ByteUnit.TiB)
                    .put("p", ByteUnit.PiB)
                    .put("pb", ByteUnit.PiB)
                    .build();

    /**
     * Convert a passed time string (e.g. 50s, 100ms, or 250us) to a time count in the given unit.
     * The unit is also considered the default if the given string does not specify a unit.
     */
    public static long timeStringAs(String str, TimeUnit unit) {
        String lower = str.toLowerCase().trim();

        try {
            Matcher m = Pattern.compile("(-?[0-9]+)([a-z]+)?").matcher(lower);
            if (!m.matches()) {
                throw new NumberFormatException("Failed to parse time string: " + str);
            }

            long val = Long.parseLong(m.group(1));
            String suffix = m.group(2);

            // Check for invalid suffixes
            if (suffix != null && !timeSuffixes.containsKey(suffix)) {
                throw new NumberFormatException("Invalid suffix: \"" + suffix + "\"");
            }

            // If suffix is valid use that, otherwise none was provided and use the default passed
            return unit.convert(val, suffix != null ? timeSuffixes.get(suffix) : unit);
        } catch (NumberFormatException e) {
            String timeError = "Time must be specified as seconds (s), " +
                    "milliseconds (ms), microseconds (us), minutes (m or min), hour (h), or day (d). " +
                    "E.g. 50s, 100ms, or 250us.";

            throw new NumberFormatException(timeError + "\n" + e.getMessage());
        }
    }

    /**
     * Convert a time parameter such as (50s, 100ms, or 250us) to milliseconds for internal use. If
     * no suffix is provided, the passed number is assumed to be in ms.
     */
    public static long timeStringAsMs(String str) {
        return timeStringAs(str, TimeUnit.MILLISECONDS);
    }

    /**
     * Convert a time parameter such as (50s, 100ms, or 250us) to seconds for internal use. If
     * no suffix is provided, the passed number is assumed to be in seconds.
     */
    public static long timeStringAsSec(String str) {
        return timeStringAs(str, TimeUnit.SECONDS);
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100kb, or 250mb) to the given. If no suffix is
     * provided, a direct conversion to the provided unit is attempted.
     */
    public static long byteStringAs(String str, ByteUnit unit) {
        String lower = str.toLowerCase().trim();

        try {
            Matcher m = Pattern.compile("([0-9]+)([a-z]+)?").matcher(lower);
            Matcher fractionMatcher = Pattern.compile("([0-9]+\\.[0-9]+)([a-z]+)?").matcher(lower);

            if (m.matches()) {
                long val = Long.parseLong(m.group(1));
                String suffix = m.group(2);

                // Check for invalid suffixes
                if (suffix != null && !byteSuffixes.containsKey(suffix)) {
                    throw new NumberFormatException("Invalid suffix: \"" + suffix + "\"");
                }

                // If suffix is valid use that, otherwise none was provided and use the default passed
                return unit.convertFrom(val, suffix != null ? byteSuffixes.get(suffix) : unit);
            } else if (fractionMatcher.matches()) {
                throw new NumberFormatException("Fractional values are not supported. Input was: "
                        + fractionMatcher.group(1));
            } else {
                throw new NumberFormatException("Failed to parse byte string: " + str);
            }

        } catch (NumberFormatException e) {
            String byteError = "Size must be specified as bytes (b), " +
                    "kibibytes (k), mebibytes (m), gibibytes (g), tebibytes (t), or pebibytes(p). " +
                    "E.g. 50b, 100k, or 250m.";

            throw new NumberFormatException(byteError + "\n" + e.getMessage());
        }
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100k, or 250m) to bytes for
     * internal use.
     *
     * If no suffix is provided, the passed number is assumed to be in bytes.
     */
    public static long byteStringAsBytes(String str) {
        return byteStringAs(str, ByteUnit.BYTE);
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100k, or 250m) to kibibytes for
     * internal use.
     *
     * If no suffix is provided, the passed number is assumed to be in kibibytes.
     */
    public static long byteStringAsKb(String str) {
        return byteStringAs(str, ByteUnit.KiB);
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100k, or 250m) to mebibytes for
     * internal use.
     *
     * If no suffix is provided, the passed number is assumed to be in mebibytes.
     */
    public static long byteStringAsMb(String str) {
        return byteStringAs(str, ByteUnit.MiB);
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100k, or 250m) to gibibytes for
     * internal use.
     *
     * If no suffix is provided, the passed number is assumed to be in gibibytes.
     */
    public static long byteStringAsGb(String str) {
        return byteStringAs(str, ByteUnit.GiB);
    }

    /**
     * Returns a byte array with the buffer's contents, trying to avoid copying the data if
     * possible.
     */
    public static byte[] bufferToArray(ByteBuffer buffer) {
        if (buffer.hasArray() && buffer.arrayOffset() == 0 &&
                buffer.array().length == buffer.remaining()) {
            return buffer.array();
        } else {
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return bytes;
        }
    }
}
