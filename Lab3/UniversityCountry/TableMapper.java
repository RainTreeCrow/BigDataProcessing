import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class TableMapper extends Mapper<LongWritable, Text, Text,TableBean> {

    private String fileName;
    private Text outK = new Text();
    private  TableBean outV = new TableBean();
    /*
    获取每个文件文件名，放在setup中只需要获取一次，在Map中则需每获取一行数据重新获取一次文件名
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //初始化 country university
        FileSplit split = (FileSplit) context.getInputSplit();

        fileName = split.getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1.获取一行数据
        String line = value.toString();
        //2.判断是哪个文件
        if (fileName.contains("university")){
            String[] split = line.split("\\|");

            outK.set(split[2]);//国家编码2
            outV.setU_key(Integer.parseInt(split[0]));
            outV.setU_name(split[1]);
            outV.setU_webpage(split[4]);
            outV.setFlag("university");

            outV.setN_name("");

        }else if(fileName.contains("country")){
            String[] split = line.split("\\|");

            outK.set(split[0]);//国家编码2
            outV.setN_name(split[1]);

            outV.setU_key(-1);
            outV.setU_name("");
            outV.setU_webpage("");
            outV.setFlag("country");
        }

        context.write(outK,outV);
    }
}
