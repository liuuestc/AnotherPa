package io;

import java.util.ArrayList;
import java.util.UUID;

// 用于上传到ps-server端和下载的参数
public class ParameterManager<T> {
    private int id = UUID.randomUUID().hashCode();// 每个
    private ArrayList<ArrayList<T>> arrayLists;
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

    public ArrayList<ArrayList<T>> getArrayLists() {
        return arrayLists;
    }

    public void setArrayLists(ArrayList<ArrayList<T>> arrayLists) {
        this.arrayLists = arrayLists;
    }
}
