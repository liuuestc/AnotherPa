package io;

import common.workerInfo.WorkerId;
import org.apache.hadoop.fs.Path;

import java.util.ArrayList;

public class ModelInfo {
    private int iter = 0;      //目前训练的次数
    private Path cachePath;  // 缓存的地址
    private WorkerId workerId;  //目前所在的worker
    private ArrayList<String> trainedList = new ArrayList(); //每一轮已经训练过的worker数据


    public ArrayList<String> getTrainedList(int n) {
        cleanTrainList(n);
        return trainedList;
    }

    public void cleanTrainList(int n){
        if (n == trainedList.size())
            trainedList = new ArrayList<String>();
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
