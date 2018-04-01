package io;


import java.util.ArrayList;

//Model 数据类型,在每台worker上传输的模型,或者根据protobuf

public class ModelManager<T> {
    private ModelInfo modelInfo;

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public ArrayList<ArrayList<T>> getModelParameters() {
        return ModelParameters;
    }

    public void setModelParameters(ArrayList<ArrayList<T>> modelParameters) {
        ModelParameters = modelParameters;
    }

    private ArrayList<ArrayList<T>> ModelParameters;

    public ArrayList<ArrayList<T>> getArrayList() {
        return ModelParameters;
    }

    public void setArrayList(ArrayList<ArrayList<T>> arrayList) {
        this.ModelParameters = arrayList;
    }
}
