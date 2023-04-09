package pro.sky.kutin.coursework3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.kutin.coursework3.model.SOCKS_COLOR;
import pro.sky.kutin.coursework3.model.SOCKS_SIZE;
import pro.sky.kutin.coursework3.model.Socks;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocksDTO {
    private SOCKS_COLOR color;
    private SOCKS_SIZE size;
    private int composition;
    private int quantity;

    public static SocksDTO buildFromSocks(Socks socks, int quantity) {
        return new SocksDTO(socks.getColor(), socks.getSize(), socks.getComposition(), quantity);
    }
    public Socks createSocks() {
        return new Socks(color, size, composition);
    }
}
