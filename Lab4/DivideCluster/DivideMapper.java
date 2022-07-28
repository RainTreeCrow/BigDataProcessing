package DivideCluster;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;

public class DivideMapper extends Mapper<Object, Text, IntWritable, VectorBean>
{
    ArrayList<double[]> centers;

    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException
    {
        Configuration config = context.getConfiguration();
        String centerPath = config.get("centerPath");
        centers = DivideUtils.readCenters(centerPath);
    }

    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException
    {
        double minDistance = Double.MAX_VALUE;
        double temp, distance;
        int index = -1;
        String[] tmpStr = value.toString().split(": ");
        int selfIndex = new Integer(tmpStr[0]);
        String[] vectorStr = tmpStr[1].split(" ");
        double[] vectorDbl = new double[15];
        for (int i = 0; i < 15; i++)
            vectorDbl[i] = new Double(vectorStr[i]);
        for (int i = 0; i < centers.size(); i++) {
            distance = 0;
            for (int j = 0; j < 15; j++) {
                temp = centers.get(i)[j];
                distance += Math.pow(vectorDbl[j] - temp, 2);
            }
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
            }
        }
        IntWritable clusterID = new IntWritable(index);
        VectorBean p = new VectorBean();
        p.setIndex(selfIndex);
        p.setVector(vectorDbl);
        context.write(clusterID, p);
    }
}