package InvertedIndex;

import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class InvertedIndexMapper extends Mapper<Object, Text, Text, IntWritable>
{
    @Override
    protected void map(Object key, Text value, Context context)
        throws IOException, InterruptedException
    {
        FileSplit fileSplit = (FileSplit)context.getInputSplit();
        String fileName = fileSplit.getPath().getName();
        Hashtable<String, Integer> F = new Hashtable<>();
        StringTokenizer itr = new StringTokenizer(value.toString());
        while (itr.hasMoreTokens())
        {
            String word = itr.nextToken();
            if(!F.containsKey(word))
                F.put(word, 1);
            else
                F.put(word, F.get(word) + 1);
        }
        for (String word: F.keySet())
        {
            Text pair = new Text(word + "," + fileName);
            IntWritable count = new IntWritable(F.get(word));
            context.write(pair, count);
        }
    }
}
