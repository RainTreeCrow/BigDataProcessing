package Sort;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.DecimalFormat;

public class SortReducer extends Reducer<FloatWritable, Text, Text, Text>
{
    DecimalFormat DF = new DecimalFormat("#.000");
    @Override
    protected void reduce(FloatWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException
    {
        for (Text value : values) {
            float real = -key.get();
            Text real_key = new Text(DF.format(real));
            context.write(value, real_key);
        }
    }
}
