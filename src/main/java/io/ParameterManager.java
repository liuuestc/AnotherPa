package io;

import protobuf.MatrixLong;

import java.util.UUID;

// 用于上传到ps-server端和下载的参数
public class ParameterManager {
    private final int id = UUID.randomUUID().hashCode();// 每个
    private MatrixLong.Matrix matrix;
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

    public MatrixLong.Matrix getArrayLists() {
        return matrix;
    }

    public void setArrayLists(MatrixLong.Matrix matrix) {
        this.matrix = matrix;
    }
}
