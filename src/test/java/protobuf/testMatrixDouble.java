package protobuf;

import java.util.List;
import java.util.Random;

public class testMatrixDouble {
    public static void main(String[] args){
        Random random = new Random(1000);
        MatrixDouble.DRow.Builder builderRow = MatrixDouble.DRow.newBuilder();
        MatrixDouble.DMatrix.Builder builder = MatrixDouble.DMatrix.newBuilder();
        builder.setId(1);
        for (long j =0 ; j < 10; j++){
            builderRow.setRowNum(j);
            for (long i = 0 ; i < 3 ; i++){
                builderRow.addNum(random.nextDouble());
            }
            builder.addRow(builderRow.build());
            builderRow.clear();
        }
        List<MatrixDouble.DRow> matrix = builder.build().getRowList();
        System.out.println(builder.build().getId());
        for (MatrixDouble.DRow dRow : matrix){
            for (double d : dRow.getNumList()){
                System.out.print(d + " ");
            }
            System.out.println();
        }
    }
}
