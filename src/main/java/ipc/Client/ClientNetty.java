package ipc.Client;

import protobuf.MatrixLong;

public interface ClientNetty {
     boolean sendParameter(MatrixLong.Matrix Matrix);
     boolean sendModel(MatrixLong.Matrix matrix);
}
