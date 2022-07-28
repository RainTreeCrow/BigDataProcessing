import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class P2Mapper extends Mapper<LongWritable, Text, CharacterPair, IntWritable>
{
    private HashMap<String, String> nicknames = new HashMap<>();
    private HashMap<String,String[]> uniMap = new HashMap<>();
    private CharacterPair outK = new CharacterPair();

    @Override
    protected void setup(Context context) {
        nicknames.put("唐僧", new String("唐三藏\n" + "陈玄奘\n" +
                "玄奘\n" +
                "唐长老\n" +
                "金蝉子\n" +
                "旃檀功德佛\n" +
                "江流儿\n" +
                "江流"));
        nicknames.put("孙悟空",new String("悟空\n" +
                "齐天大圣\n" +
                "美猴王\n" +
                "猴王\n" +
                "斗战胜佛\n" +
                "孙行者\n" +
                "心猿\n" +
                "金公"));
        nicknames.put("猪八戒",new String("猪悟能\n" +
                "悟能\n" +
                "八戒\n" +
                "猪刚鬣\n" +
                "老猪\n" +
                "净坛使者\n" +
                "天蓬元帅\n" +
                "木母"));
        nicknames.put("沙僧",new String("沙和尚\n" +
                "沙悟净\n" +
                "悟净\n" +
                "金身罗汉\n" +
                "卷帘大将\n" +
                "刀圭"));
        nicknames.put("白龙马","小白龙\n" +
                "白马\n" +
                "八部天龙马");
        nicknames.put("如来佛祖", "如来");
        nicknames.put("观音菩萨","观音\n" +
                "观世音菩萨\n" +
                "观世音");
        nicknames.put("玉帝","玉皇大帝");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString();
        String[] splits = line.split(" ");
        HashSet<String> roles = new HashSet<>();
        for(int i = 0; i < splits.length; i++){
            int flag = 0;
            for (Map.Entry<String,String> nick_item : nicknames.entrySet()){
                String all = nick_item.getValue();
                if(all.indexOf(splits[i]) != -1) {
                    roles.add(nick_item.getKey());
                    flag = 1;
                    break;
                }
            }
            if(flag != 1) {
                roles.add(splits[i]);
            }
        }
        for (String c1: roles) {
            for(String c2: roles){
                if(c1.equals(c2))
                    continue;
                outK.setCharacter_1(c1);
                outK.setCharacter_2(c2);
                context.write(outK,new IntWritable(1));
            }
        }
    }
}