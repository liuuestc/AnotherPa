package io;


import protobuf.MatrixLong;

import java.util.List;

//根据管理所有的BlockInfo,并根据worker数量将BlockInfo分配
public class BlockMangerImpl implements BlockManager   {


    @Override
    public List<BlockInfo> getAndAllocBlockInfo(String path, int n) {
        return null;
    }

    @Override
    public boolean loadData(MatrixLong.Matrix matrix, BlockInfo blockInfo) {
        return false;
    }

    @Override
    public boolean writeToHDFS(MatrixLong.Matrix matrix, String path) {
        return false;
    }
}
