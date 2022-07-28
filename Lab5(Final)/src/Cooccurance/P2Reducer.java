import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class P2Reducer extends Reducer<CharacterPair,IntWritable, CharacterPair, IntWritable>
{
    @Override
    protected void reduce(CharacterPair key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable it: values) {
            sum += it.get();
        }
        context.write(key,new IntWritable(sum));
    }
}
