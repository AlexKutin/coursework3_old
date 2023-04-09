package pro.sky.kutin.coursework3.services;

import pro.sky.kutin.coursework3.dto.SocksDTO;

public interface SocksService {
    boolean acceptSocks(SocksDTO socksDTO);

    int getQuantity(String color, String size, Integer cottonMin, Integer cottonMax);

    boolean releaseSocks(SocksDTO socksDTO);

    boolean writeOffSocks(SocksDTO socksDTO);

    void readSocksStoreFromFile();

    void readOperationsFromFile();
}
