package io;

import protobuf.MatrixLong;

import java.util.List;

public interface BlockManager {
    //根据地址获得文件的BlockInfo并根据分配机器数目将blockInfo分组，及将里面的groupid赋值
    boolean getAndAllocBlockInfo(String path,int n);
    //根据BlockInfo将数据读入matrix中
    boolean loadData(BlockInfo blockInfo);
    //SequenceFile格式写出
    boolean writeToHDFS(MatrixLong.Matrix matrix, String path);
}
