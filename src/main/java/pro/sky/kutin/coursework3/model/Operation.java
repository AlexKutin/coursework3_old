package pro.sky.kutin.coursework3.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.kutin.coursework3.dto.SocksDTO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Operation {
    private OPERATION_TYPE operationType;
    private LocalDateTime dateTime;
    private SOCKS_COLOR color;
    private SOCKS_SIZE size;
    private int composition;
    private int quantity;

    public static Operation buildFromSockDTO(OPERATION_TYPE operationType, SocksDTO socksDTO) {
        return new Operation(operationType, LocalDateTime.now(), socksDTO.getColor(), socksDTO.getSize(),
                socksDTO.getComposition(), socksDTO.getQuantity());
    }
}
