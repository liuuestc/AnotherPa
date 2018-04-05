package ipc;

import protobuf.MatrixLong;

import java.util.List;

public interface ServerNetty {
    int initial();  //初始化ServerNetty 并返回port
    List<MatrixLong.Row> getMatrix();
}
