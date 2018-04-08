package common.workerInfo;

import java.util.LinkedList;
import java.util.List;

//临时使用
public class WorkerInfo {
    private Long id;
    private String localhost;
    private int nettyPort;
    private String blockInfo;
    private List<Long> modelIds = new LinkedList<Long>();
    private Long parasId;
    private boolean Loaded = false;
    private double bias = 0;
    private boolean inited = false;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean running = false;

    public boolean isInited() {
        return inited;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public boolean isLoaded() {
        return Loaded;
    }
    public void setLoaded(boolean loaded) {
        Loaded = loaded;
    }
    public void setModelIds(List<Long> modelIds) {
        this.modelIds = modelIds;
    }

    public List<Long> getModelIds() {
        return modelIds;
    }

    public void addModelIds(Long modelId) {
        this.modelIds.add(modelId);
    }

    public Long getParasId() {
        return parasId;
    }

    public void setParasId(Long parasId) {
        this.parasId = parasId;
    }

    public String getBlockInfo() {
        return blockInfo;
    }

    public void setBlockInfo(String blockInfo) {
        this.blockInfo = blockInfo;
    }

    public WorkerInfo(Long id, String localhost, int nettyPort) {
        this.id = id;
        this.localhost = localhost;
        this.nettyPort = nettyPort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalhost() {
        return localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    public int getNettyPort() {
        return nettyPort;
    }

    public void setNettyPort(int nettyPort) {
        this.nettyPort = nettyPort;
    }
}
