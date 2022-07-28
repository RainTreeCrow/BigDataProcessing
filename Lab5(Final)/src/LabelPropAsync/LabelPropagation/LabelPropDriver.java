package LabelPropagation;

import java.io.IOException;

public class LabelPropDriver
{
    private static int times = 20;

    public static void main(String[] args) throws IOException
    {
        if (args.length > 2)
            times = Integer.parseInt(args[2]);
        String[] forLGB = {"", args[1] + "/Round0"};
        forLGB[0] = args[0];
        LabelGraphBuilder.LabelGraphBuilder.main(forLGB);
        String[] forItr = {"Round" , "Round"};
        int i;
        for (i = 0; i < times; i++) {
            forItr[0] = args[1] + "/Round" + (i);
            forItr[1] = args[1] + "/Round" + (i + 1);
            LabelPropagationIter.LabelPropIter.main(forItr, i + 1);
        }
        String[] forRV = {args[1] + "/Round" + times, args[1] + "/Communities"};
        LabelPropagationViewer.LabelPropViewer.main(forRV);
    }
}
