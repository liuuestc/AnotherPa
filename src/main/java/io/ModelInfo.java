package io;


import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ModelInfo {

    private long modelId = UUID.randomUUID().getMostSignificantBits(); //Model唯一的编号
    private int iter = 0;     //目前训练的次数
    private String cachePath;   // 缓存的地址
    private Long workerId;    //目前所在的worker
    private long matrixId;    //模型的matrixId
    private boolean finished = false;    //本模型是否训练完成
    private List<Long> round;      //只在master端实例化
    private double bias = 0;       //本模型的误差

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public void initialRound(){
        this.round = new LinkedList<Long>();
    }
    public List<Long> getRound() {
        return round;
    }
    public void addThisRound(Long thisRound) {
        this.round.add(thisRound);
    }
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public long getModelId() {
        return modelId;
    }

    public void setMatrixId(long matrixId) {
        this.matrixId = matrixId;
    }
        public long getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(Long matrixId) {
        this.matrixId = matrixId;
    }

    public int getIter() {
        return iter;
    }

    public void setIter(int iter) {
        this.iter = iter;
    }

    public String  getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

}
