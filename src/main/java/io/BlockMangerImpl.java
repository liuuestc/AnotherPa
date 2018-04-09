package io;


import protobuf.Matrix;
import protobuf.MatrixLong;

import java.util.LinkedList;
import java.util.List;

//根据管理所有的BlockInfo,并根据worker数量将BlockInfo分配,并且管理所有的Block
public class BlockMangerImpl implements BlockManager   {

    private List<MatrixLong.Matrix> parameters = new LinkedList<MatrixLong.Matrix>();
    private List<MatrixLong.Matrix> models = new LinkedList<MatrixLong.Matrix>();
    private List<MatrixLong.Matrix> trainData = new LinkedList<MatrixLong.Matrix>();
    private List<BlockInfo> infos = new LinkedList<BlockInfo>();

    public void addParamters(MatrixLong.Matrix parameter) {
        parameters.add(parameter);
    }
    public void addModels(MatrixLong.Matrix model){
        models.add(model);
    }

    public List<MatrixLong.Matrix> getParameters() {
        return parameters;
    }

    public List<MatrixLong.Matrix> getModels() {
        return models;
    }

    public List<MatrixLong.Matrix> getTrainData() {
        return trainData;
    }

    public List<BlockInfo> getInfos() {
        return infos;
    }
    //返回String格式的infos
    public String getInfoString(){
        return "";
    }
    @Override
    public boolean getAndAllocBlockInfo(String path, int n) {
        //将List<BlockInfo>赋给this.infos

        return false;
    }

    @Override
    public boolean loadData(BlockInfo blockInfo)
    {
        //将数据加载到trainData中
        return false;
    }

    @Override
    public boolean writeToHDFS(MatrixLong.Matrix matrix, String path) {
        return false;
    }


}
