package KMeans;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class KMeansReducer extends Reducer<IntWritable, VectorBean, Text, NullWritable>
{
    @Override
    protected void reduce(IntWritable key, Iterable<VectorBean> values, Context context)
            throws IOException, InterruptedException
    {
        double[] vectorDbl = new double[15];
        for (int i = 0; i < 15; i++)
            vectorDbl[i] = 0;
        int count = 0;
        Iterator<VectorBean> it = values.iterator();
        while (it.hasNext()) {
            VectorBean next = it.next();
            double[] tempVector = next.getVector();
            for (int i = 0; i < 15; i++)
                vectorDbl[i] += tempVector[i];
            count += next.getCount();
        }
        for (int i = 0; i < 15; i++)
            vectorDbl[i] /= count;
        VectorBean pm = new VectorBean();
        pm.setVector(vectorDbl);
        pm.setCount(count);
        StringBuilder str = new StringBuilder(key.toString());
        str.append(": ").append(pm);
        Text line = new Text(str.toString());
        context.write(line, NullWritable.get());
    }
}
