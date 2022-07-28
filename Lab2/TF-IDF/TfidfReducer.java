package Tfidf;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TfidfReducer extends Reducer<Text, Text, Text, NullWritable>
{
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException
    {
        Iterator<Text> it = values.iterator();
        while (it.hasNext()) {
            Text all = new Text(key.toString() + ", " + it.next().toString());
            context.write(all, null);
        }
    }
}
