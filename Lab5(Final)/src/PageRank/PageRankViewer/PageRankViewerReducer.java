package PageRankViewer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.DecimalFormat;

public class PageRankViewerReducer extends Reducer<DoubleWritable, Text, Text, Text>
{
    DecimalFormat DF = new DecimalFormat("#.0000000000");
    @Override
    protected void reduce(DoubleWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException
    {
        for (Text value : values) {
            Text formated_key = new Text(DF.format(-key.get()));
            context.write(value, formated_key);
        }
    }
}