package KMeans;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.DecimalFormat;

public class OutputMapper extends Mapper<Object, Text, Text, NullWritable>
{
    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException
    {
        DecimalFormat DF = new DecimalFormat("#.000");
        String[] tmpStr = value.toString().split(": ");
        String[] vectorStr = tmpStr[1].split(",");
        double[] vectorDbl = new double[15];
        for (int i = 0; i < 15; i++)
            vectorDbl[i] = new Double(vectorStr[i]);
        StringBuilder str = new StringBuilder(tmpStr[0]);
        str.append("\t");
        for (int i = 0; i < 14; i++)
            str.append(DF.format(vectorDbl[i])).append(",");
        str.append(DF.format(vectorDbl[14]));
        context.write(new Text(str.toString()), NullWritable.get());
    }
}
