package LabelPropagationViewer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LabelPropViewerMapper extends Mapper<Object, Text, IntWritable, Text>
{
    @Override
    public void map(Object key, Text value, Mapper.Context context)
            throws IOException, InterruptedException {
        String[] tmpStr = value.toString().split("\t");
        int label = Integer.parseInt(tmpStr[0]);
        String name = tmpStr[1];
        context.write(new IntWritable(label), new Text(name));
    }
}