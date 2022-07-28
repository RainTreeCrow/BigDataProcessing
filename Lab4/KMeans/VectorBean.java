package KMeans;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class VectorBean implements Writable
{
    double[] vector = new double[15];
    int count = 0;

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vec) {
        System.arraycopy(vec, 0, vector, 0, 15);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int c) {
        count = c;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        for (int i = 0; i < 15; i++)
            dataOutput.writeDouble(vector[i]);
        dataOutput.writeInt(count);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        for (int i = 0; i < 15; i++)
            vector[i] = dataInput.readDouble();
        count = dataInput.readInt();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 14; i++)
            result.append(vector[i]).append(",");
        result.append(vector[14]);
        return result.toString();
    }
}
