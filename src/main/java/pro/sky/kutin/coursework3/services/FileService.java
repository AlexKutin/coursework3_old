package pro.sky.kutin.coursework3.services;

import java.io.File;

public interface FileService {
    boolean saveSocksStoreToFile(String json);

    String readWarehouseFromFile();

    File getWarehouseDataFile();

    File getOperationsDataFile();

    String readOperationsFromFile();

    boolean saveOperationsToFile(String json);

    boolean cleanWarehouseDataFile();

    boolean cleanOperationsLogDataFile();
}
