package pro.sky.kutin.coursework3.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socks {
    @JsonProperty("Color")
    private SOCKS_COLOR color;
    @JsonProperty("Size")
    private SOCKS_SIZE size;
    private int composition;

    public boolean isMatch(SocksPattern socksPattern) throws IllegalArgumentException {
        boolean isMatch = true;
        if (socksPattern.getColor() != null) {
            isMatch = this.color == SOCKS_COLOR.valueOf(socksPattern.getColor());
        }

        if (isMatch && socksPattern.getSize() != null) {
            isMatch = this.size == SOCKS_SIZE.fromText(socksPattern.getSize());
        }

        Integer cottonMin = socksPattern.getCottonMin();
        if (isMatch && cottonMin != null) {
            isMatch = this.composition >= cottonMin;
        }

        Integer cottonMax = socksPattern.getCottonMax();
        if (isMatch && cottonMax != null) {
            isMatch = this.composition <= cottonMax;
        }
        return isMatch;
    }

}
