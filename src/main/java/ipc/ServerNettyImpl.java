package ipc;

import protobuf.MatrixLong;

public class ServerNettyImpl implements ServerNetty , Runnable {


    @Override
    public void run() {

    }

    @Override
    public int initial() {
        return 0;
    }

    @Override
    public MatrixLong.Matrix getMatrix() {
        return null;
    }
}
