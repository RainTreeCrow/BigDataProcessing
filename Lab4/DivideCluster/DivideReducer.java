package DivideCluster;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;

public class DivideReducer extends Reducer<IntWritable, VectorBean, NullWritable, VectorBean>
{
    private MultipleOutputs mos;

    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException
    {
        mos = new MultipleOutputs(context);
    }

    @Override
    protected void reduce(IntWritable key, Iterable<VectorBean> values, Context context)
            throws IOException, InterruptedException
    {
        String clusterID = "cluster" + key.toString();
        for (VectorBean vector : values) {
            mos.write(clusterID, NullWritable.get(), vector);
        }
    }

    @Override
    protected void cleanup(Context context)
            throws IOException, InterruptedException
    {
        mos.close();
    }
}