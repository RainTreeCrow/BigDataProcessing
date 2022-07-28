import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;


public class TableMapper extends Mapper<LongWritable, Text, TableBean, NullWritable> {


    private HashMap<String,String[]> uniMap = new HashMap<>();

    private TableBean outK = new TableBean();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //获取缓存文件地址 university.tbl
        URI[] cacheFiles = context.getCacheFiles();
        //获取文件内容封装到集合
        FileSystem fs = FileSystem.get(context.getConfiguration());
        FSDataInputStream fis = fs.open(new Path(cacheFiles[0]));

        BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));

        String line;
        //循环读一行内容
        while (StringUtils.isNotEmpty(line = reader.readLine())) {
            //切割
            String[] fields = line.split("\\|");

            String[] uniInfo = new String[2];
            uniInfo[0] = fields[1];//u_name
            uniInfo[1] = fields[4];//u_webpage
            //赋值
            uniMap.put(fields[0],uniInfo);

        }

        //关流
        IOUtils.closeStream(reader);
    }


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1.获取一行数据 course.tbl
        String line = value.toString();
        String[] split = line.split("\\|");

        //2.赋值结果 course.tbl
        outK.setC_key(Integer.parseInt(split[0]));
        outK.setC_name(split[1]);
        outK.setC_subject(split[2]);
        outK.setC_hours(split[3]);



        //遍历university集合进行连接join
        for (HashMap.Entry<String,String[]> uni : uniMap.entrySet()) {
            int u_key = Integer.parseInt(uni.getKey());
            String u_name = uni.getValue()[0];
            String u_web = uni.getValue()[1];

            //3.赋值结果 university.tbl
            outK.setU_key(u_key);
            outK.setU_name(u_name);
            outK.setU_webpage(u_web);

            context.write(outK,NullWritable.get());
        }

    }
}
