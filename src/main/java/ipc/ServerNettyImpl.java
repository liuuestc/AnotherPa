package ipc;

import protobuf.MatrixDouble;
import protobuf.MatrixLong;

import java.util.List;

public class ServerNettyImpl implements ServerNetty , Runnable {


    @Override
    public void run() {

    }

    @Override
    public int initial() {
        return 0;
    }

    @Override
    public List<MatrixLong.Row> getMatrix() {
        return null;
    }
}
