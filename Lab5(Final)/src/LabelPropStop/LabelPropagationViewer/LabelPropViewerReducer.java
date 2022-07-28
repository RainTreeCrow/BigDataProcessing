package LabelPropagationViewer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class LabelPropViewerReducer extends Reducer<IntWritable, Text, IntWritable, Text>
{
    @Override
    protected void reduce(IntWritable key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {
        StringBuilder members = new StringBuilder();
        for (Text member : values) {
            members.append(member.toString()).append(" ");
        }
        String result = members.toString().trim();
        context.write(key, new Text(result));
    }
}
