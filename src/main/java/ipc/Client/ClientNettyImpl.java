package ipc.Client;


import protobuf.MatrixLong.Matrix;

public class ClientNettyImpl implements ClientNetty{
    @Override
    public boolean sendParameter(Matrix matrix) {
        return false;
    }

    @Override
    public boolean sendModel(Matrix matrix) {
        return false;
    }
}
