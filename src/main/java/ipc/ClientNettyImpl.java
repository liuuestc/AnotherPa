package ipc;

import protobuf.MatrixDouble;
import protobuf.MatrixLong;

public class ClientNettyImpl implements ClientNetty{
    @Override
    public boolean sendParameter(MatrixDouble.DMatrix dMatrix) {
        return false;
    }

    @Override
    public boolean sendModel(MatrixLong.Matrix matrix) {
        return false;
    }
}
