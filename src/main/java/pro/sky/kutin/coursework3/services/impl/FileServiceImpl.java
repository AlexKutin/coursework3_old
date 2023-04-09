package pro.sky.kutin.coursework3.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.kutin.coursework3.services.FileService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {
    @Value("${path.to.data.file}")
    private String dataFilePath;

    @Value("${name.of.socks.warehouse.data.file}")
    private String socksWarehouseDataFileName;

    @Value("${name.of.operations.data.file}")
    private String operationsDataFileName;

    private Path socksWarehouseDataPath;
    private Path operationsDataPath;

    @PostConstruct
    private void init() {
        socksWarehouseDataPath = Path.of(dataFilePath, socksWarehouseDataFileName);
        operationsDataPath = Path.of(dataFilePath, operationsDataFileName);
    }

    @Override
    public boolean saveSocksStoreToFile(String json) {
        try {
            if (cleanDataFile(socksWarehouseDataPath)) {
                Files.writeString(socksWarehouseDataPath, json);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String readWarehouseFromFile() {
        try {
            if (Files.exists(socksWarehouseDataPath)) {
                return Files.readString(socksWarehouseDataPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public File getWarehouseDataFile() {
        return new File(dataFilePath + "\\" + socksWarehouseDataFileName);
    }

    @Override
    public File getOperationsDataFile() {
        return new File(dataFilePath + "\\" + operationsDataFileName);
    }

    @Override
    public String readOperationsFromFile() {
        try {
            return Files.readString(operationsDataPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean saveOperationsToFile(String json) {
        try {
            if (cleanDataFile(operationsDataPath)) {
                Files.writeString(operationsDataPath, json);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cleanWarehouseDataFile() {
        return cleanDataFile(socksWarehouseDataPath);
    }

    @Override
    public boolean cleanOperationsLogDataFile() {
        return cleanDataFile(operationsDataPath);
    }

    private boolean cleanDataFile(Path path) {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
