package ipc.Client;

import protobuf.MatrixDouble;
import protobuf.MatrixLong;

public class ClientNettyImpl implements ClientNetty{
    @Override
    public boolean sendParameter(MatrixLong.Matrix matrix) {
        return false;
    }

    @Override
    public boolean sendModel(MatrixLong.Matrix matrix) {
        return false;
    }
}
