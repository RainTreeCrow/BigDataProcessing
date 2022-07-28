import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CharacterPair implements WritableComparable<CharacterPair>
{
    private int times;
    private String character_1;
    private String character_2;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getCharacter_1() {
        return character_1;
    }

    public void setCharacter_1(String character_1) {
        this.character_1 = character_1;
    }

    public String getCharacter_2() {
        return character_2;
    }

    public void setCharacter_2(String characetr_2) {
        this.character_2 = characetr_2;
    }

    public CharacterPair() {
        this.character_1 = "";
        this.character_2 = "";
        this.times = 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(times);
        dataOutput.writeUTF(character_1);
        dataOutput.writeUTF(character_2);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.times = dataInput.readInt();
        this.character_1 = dataInput.readUTF();
        this.character_2 = dataInput.readUTF();
    }

    @Override
    public String toString() {
        return  '<' + character_1
                + '，' + character_2
                + '>';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CharacterPair) {
            CharacterPair tp = (CharacterPair) obj;
            return (character_1.equals(tp.character_1) && character_2.equals(tp.character_2)) ||
                    (character_1.equals(tp.character_2) && character_2.equals(tp.character_1));
        }
        return false;
    }

    @Override
    public int compareTo(CharacterPair cp) {
        int cmp = character_1.compareTo(cp.character_1);
        if(cmp !=0)
            return cmp;
        else
            return character_2.compareTo(cp.character_2);
    }

    @Override
    public int hashCode() {
        Integer code = character_1.hashCode() * 163 + character_2.hashCode();
        System.out.println(code.intValue());
        return code;
    }
}