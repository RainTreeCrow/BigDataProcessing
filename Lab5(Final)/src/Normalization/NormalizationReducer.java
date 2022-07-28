import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class NormalizationReducer extends Reducer<Text, ValuePair, Text, Text>
{
    @Override
    protected void reduce(Text key, Iterable<ValuePair> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        int num = 0;
        ArrayList<ValuePair> tmp_values = new ArrayList<>();
        for (ValuePair it: values) {
            sum += it.getValue();
            ++num;
            tmp_values.add(new ValuePair(it));
        }
        String pairs = "[";
        DecimalFormat decimalFormat= new DecimalFormat( "0.#####" );
        int index = 0;
        for (ValuePair it :tmp_values){
            ++index;
            if(index < num)
                pairs += it.getName() + "，" +
                        decimalFormat.format((float) it.getValue()/sum) + "|";
            else
                pairs += it.getName() + "，" +
                        decimalFormat.format((float) it.getValue()/sum) + "]";
        }
        context.write(key,new Text(pairs));
    }
}