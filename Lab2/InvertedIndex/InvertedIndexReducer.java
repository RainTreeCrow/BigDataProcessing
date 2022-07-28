package InvertedIndex;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InvertedIndexReducer extends Reducer<Text, IntWritable, Text, Text>
{
    private static String currentWord;
    private static int wordCount;
    private static int fileCount;
    StringBuilder all;
    DecimalFormat DF;

    @Override
    protected void setup(Context context)
    {
        currentWord = null;
        wordCount = 0;
        fileCount = 0;
        all = new StringBuilder();
        DF = new DecimalFormat("#.000");
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException
    {
        String[] split = key.toString().split(",");
        String word = split[0];
        String fileName = split[1];
        Iterator<IntWritable> it = values.iterator();
        if (currentWord != null && !word.equals(currentWord))
        {
            Text tmpWord = new Text(currentWord);
            float average = (float) wordCount / fileCount;
            Text tmpAverage = new Text(DF.format(average)+"\t"+all.toString());
            context.write(tmpWord, tmpAverage);
            wordCount = 0;
            fileCount = 0;
            all.delete(0, all.length());
        }
        else if (currentWord != null)
            all.append("; ");
        fileCount++;
        int tmpCount = 0;
        while (it.hasNext())
            tmpCount += it.next().get();
        wordCount += tmpCount;
        all.append(fileName).append(":").append(tmpCount);
        currentWord = word;
    }

    @Override
    protected void cleanup(Context context)
        throws IOException, InterruptedException
    {
        Text tmpWord = new Text(currentWord);
        float average = (float) wordCount / fileCount;
        Text tmpAverage = new Text(average+"\t"+all.toString());
        context.write(tmpWord, tmpAverage);
    }
}
