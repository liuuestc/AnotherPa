package io;

import org.apache.hadoop.fs.Path;

import java.util.ArrayList;

//根据管理所有的BlockInfo,并根据worker数量将BlockInfo分配
public class BlockManger {
    private ArrayList<BlockInfo> blockInfo;
    private Path path;


    public ArrayList<BlockInfo> getBlockInfo() {
        return blockInfo;
    }

    public void setBlockInfo(ArrayList<BlockInfo> blockInfo) {
        this.blockInfo = blockInfo;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    //获取所有的BlockInfo
    public void getBlockInfo(Path path){

    }
}
