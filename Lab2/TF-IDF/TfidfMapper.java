package Tfidf;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TfidfMapper extends Mapper<Object, Text, Text, Text>
{
    private static int totalFileCount;
    DecimalFormat DF;

    @Override
    protected void setup(Context context)
    {
        Configuration config = context.getConfiguration();
        totalFileCount = Integer.parseInt(config.get("total"));
        DF = new DecimalFormat("#.000");
    }

    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException
    {
        StringTokenizer itr = new StringTokenizer(value.toString());
        Hashtable<String, Integer> tfTable = new Hashtable<>();
        int fileCount = 0;
        if (itr.hasMoreTokens()) {
            String word = itr.nextToken();
            if (itr.hasMoreTokens()) {
                itr.nextToken();
                while (itr.hasMoreTokens()) {
                    fileCount++;
                    String pair = itr.nextToken();
                    String workName = pair.split(":")[0].split("-")[0];
                    int wordCount = new Integer(pair.split(":")[1].split(";")[0]);
                    if (!tfTable.containsKey(workName))
                        tfTable.put(workName, wordCount);
                    else
                        tfTable.put(workName, tfTable.get(workName) + wordCount);
                }
                for (String workName: tfTable.keySet()) {
                    Text work = new Text(workName);
                    double tf = (double)tfTable.get(workName);
                    double inf = Math.log((double) totalFileCount / (fileCount + 1));
                    Text pair = new Text(word + ", " + DF.format(tf * inf));
                    context.write(work, pair);
                }
            }
        }
    }
}
