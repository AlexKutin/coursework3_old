package pro.sky.kutin.coursework3.services;

import pro.sky.kutin.coursework3.dto.SocksDTO;

public interface SocksService {
    boolean registerSocks(SocksDTO socksDTO);

    int getQuantity(String color, String size, Integer cottonMin, Integer cottonMax);

    boolean releaseSocks(SocksDTO socksDTO);
}
