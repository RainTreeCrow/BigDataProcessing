package Sort;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class SortMapper extends Mapper<Object, Text, FloatWritable, Text>
{
    @Override
    protected void map(Object key, Text value, Context context)
        throws IOException, InterruptedException
    {
        StringTokenizer itr = new StringTokenizer(value.toString());
        if (itr.hasMoreTokens()) {
            String word = itr.nextToken();
            if (itr.hasMoreTokens()) {
                String average = itr.nextToken();
                FloatWritable avg = new FloatWritable(-(new Float(average)));
                Text term = new Text(word);
                context.write(avg, term);
            }
        }
    }
}
