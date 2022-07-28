package KMeans;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class KMeansCombiner extends Reducer<IntWritable, VectorBean, IntWritable, VectorBean>
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
        VectorBean pm = new VectorBean();
        pm.setVector(vectorDbl);
        pm.setCount(count);
        context.write(key, pm);
    }
}
