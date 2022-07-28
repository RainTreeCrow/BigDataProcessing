package LabelGraphBuilder;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LabelGraphBuilderMapper extends Mapper<Object,Text, IntWritable,Text>
{
    private static int index;

    @Override
    protected void setup(Context context) {
        index = 0;
    }

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        IntWritable label = new IntWritable(++index);
        context.write(label, value);
    }
}