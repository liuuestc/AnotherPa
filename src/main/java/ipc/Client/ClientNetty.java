package ipc.Client;

import protobuf.MatrixLong;

public interface ClientNetty {
     boolean sendParameter(MatrixLong.Matrix Matrix,String ip , int port);
     boolean sendModel(MatrixLong.Matrix matrix, String ip, int port);
}
