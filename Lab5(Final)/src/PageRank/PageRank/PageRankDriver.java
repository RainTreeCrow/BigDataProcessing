package PageRank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;

public class PageRankDriver
{
    private static int times = 10;

    public static int getNameCnt(String filePath) throws IOException {
        Path path = new Path(filePath);
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);
        FSDataInputStream inputStream;
        LineReader lineReader;
        FileStatus[] fileStatus = fileSystem.listStatus(path);
        Text line = new Text();
        int nameCnt = 0;
        for (int i = 0; i < fileStatus.length; i++) {
            inputStream = fileSystem.open(fileStatus[i].getPath());
            lineReader = new LineReader(inputStream, configuration);
            while (lineReader.readLine(line) > 0)
                nameCnt++;
        }
        return nameCnt;
    }

    public static void main(String[] args) throws IOException
    {
        int nameCnt = getNameCnt(args[0]);
        double pr_init = 1.0 / nameCnt;
        if (args.length > 2)
            times = Integer.parseInt(args[2]);
        String[] forGB = {"", args[1] + "/Data0"};
        forGB[0] = args[0];
        GraphBuilder.GraphBuilder.main(forGB, pr_init);
        String[] forItr = {"Data" , "Data"};
        for (int i = 0; i < times; i++) {
            forItr[0] = args[1] + "/Data" + (i);
            forItr[1] = args[1] + "/Data" + (i + 1);
            PageRankIter.PageRankIter.main(forItr, i + 1, pr_init);
        }
        String[] forRV = {args[1] + "/Data" + times, args[1] + "/FinalRank"};
        PageRankViewer.PageRankViewer.main(forRV);
    }
}
