package ipc;

import protobuf.MatrixDouble;
import protobuf.MatrixLong;

public interface ClientNetty {
     boolean sendParameter(MatrixDouble.DMatrix dMatrix);
     boolean sendModel(MatrixLong.Matrix matrix);
}
