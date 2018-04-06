package ipc.Client;


import protobuf.MatrixLong;
import protobuf.MatrixLong.Matrix;

public class ClientNettyImpl implements ClientNetty{

    @Override
    public boolean sendParameter(Matrix Matrix, String ip, int port) {
        return false;
    }

    @Override
    public boolean sendModel(Matrix matrix, String ip, int port) {
        return false;
    }
}
