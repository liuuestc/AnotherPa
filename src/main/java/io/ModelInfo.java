package io;

import common.workerInfo.WorkerId;
import org.apache.hadoop.fs.Path;
import protobuf.MatrixLong;

import java.util.ArrayList;
import java.util.UUID;

public class ModelInfo {

    private final long modelId = UUID.randomUUID().getMostSignificantBits(); //Model唯一的编号
    private int iter = 0;      //目前训练的次数
    private Path cachePath;  // 缓存的地址
    private WorkerId workerId;  //目前所在的worker
    private MatrixLong matrixLong;

    public long getModelId() {
        return modelId;
    }
    public MatrixLong getMatrixLong() {
        return matrixLong;
    }

    public void setMatrixLong(MatrixLong matrixLong) {
        this.matrixLong = matrixLong;
    }

    public int getIter() {
        return iter;
    }

    public void setIter(int iter) {
        this.iter = iter;
    }

    public Path getCachePath() {
        return cachePath;
    }

    public void setCachePath(Path cachePath) {
        this.cachePath = cachePath;
    }

    public WorkerId getWorkerId() {
        return workerId;
    }

    public void setWorkerId(WorkerId workerId) {
        this.workerId = workerId;
    }

}
