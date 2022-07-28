package InvertedIndex;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

public class NewPartitioner extends HashPartitioner<Text, IntWritable>
{
    @Override
    public int getPartition(Text key, IntWritable value, int numReduceTasks)
    {
        Text term = new Text(key.toString().split(",")[0]);
        super.getPartition(term, value, numReduceTasks);
        return numReduceTasks;
    }
}