import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class TableReduce extends Reducer<Text, TableBean, TableBean, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<TableBean> values, Context context) throws IOException, InterruptedException {
        ArrayList<TableBean> uniBeans = new ArrayList<>();
        TableBean cntryBean = new TableBean();

        for (TableBean value : values) {

            if("university".equals(value.getFlag())){

                //hadoop中迭代器value优化为一个地址，如果直接add(value)永远是指向一个对象
                TableBean tmpBean = new TableBean();

                try {
                    BeanUtils.copyProperties(tmpBean,value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

                uniBeans.add(tmpBean);
            }else {

                try {
                    BeanUtils.copyProperties(cntryBean,value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }


        }

        for (TableBean uniBean : uniBeans) {

            uniBean.setN_name(cntryBean.getN_name());

            context.write(uniBean,NullWritable.get());
        }
    }
}
