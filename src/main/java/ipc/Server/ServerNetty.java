package ipc.Server;

import protobuf.MatrixLong;

import java.util.List;

public interface ServerNetty {
    int initial();  //初始化ServerNetty 并返回port
    MatrixLong.Matrix getMatrix();
}
