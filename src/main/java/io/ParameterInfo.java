package io;

import protobuf.MatrixLong;

import java.util.UUID;

// 用于上传到ps-server端和下载的参数
public class ParameterInfo {
    private int id = UUID.randomUUID().hashCode();// 每个
    private Long matrixId;
    private int column;
    private int row;


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(Long matrixId) {
        this.matrixId = matrixId;
    }
}
