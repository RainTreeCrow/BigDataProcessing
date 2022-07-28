package DivideCluster;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class VectorBean implements Writable
{
    int index;
    double[] vector = new double[15];

    public void setIndex(int i) {
        index = i;
    }

    public void setVector(double[] vec) {
        System.arraycopy(vec, 0, vector, 0, 15);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(index);
        for (int i = 0; i < 15; i++)
            dataOutput.writeDouble(vector[i]);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        index = dataInput.readInt();
        for (int i = 0; i < 15; i++)
            vector[i] = dataInput.readDouble();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(index).append(": ");
        for (int i = 0; i < 14; i++)
            result.append((int) vector[i]).append(" ");
        result.append((int) vector[14]);
        return result.toString();
    }
}
