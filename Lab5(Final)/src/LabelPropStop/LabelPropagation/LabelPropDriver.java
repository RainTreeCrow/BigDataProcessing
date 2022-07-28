package LabelPropagation;

import java.io.IOException;

public class LabelPropDriver
{
    public static void main(String[] args) throws IOException
    {
        String[] forLGB = {"", args[1] + "/Round0"};
        forLGB[0] = args[0];
        LabelGraphBuilder.LabelGraphBuilder.main(forLGB);
        String[] forItr = {"Round" , "Round"};
        int i = 0;
        String filePath = args[1] + "/finished";
        while (true) {
            forItr[0] = args[1] + "/Round" + (i);
            forItr[1] = args[1] + "/Round" + (i + 1);
            if (LabelPropagationIter.LabelPropIter.main(forItr, i + 1, filePath))
                break;
            i++;
        }
        String[] forRV = {args[1] + "/Round" + (i + 1), args[1] + "/Communities"};
        LabelPropagationViewer.LabelPropViewer.main(forRV);
    }
}
