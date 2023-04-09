package pro.sky.kutin.coursework3.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pro.sky.kutin.coursework3.dto.SocksDTO;
import pro.sky.kutin.coursework3.model.OPERATION_TYPE;
import pro.sky.kutin.coursework3.model.Operation;
import pro.sky.kutin.coursework3.model.Socks;
import pro.sky.kutin.coursework3.model.SocksPattern;
import pro.sky.kutin.coursework3.services.FileService;
import pro.sky.kutin.coursework3.services.SocksService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pro.sky.kutin.coursework3.dto.SocksDTO.*;

@Service
public class SocksServiceImpl implements SocksService {
    private static Map<Socks, Integer> socksStore = new HashMap<>();
    private final FileService fileService;
    private final ObjectMapper mapper;
    private static List<Operation> operationList = new LinkedList<>();

    public SocksServiceImpl(FileService fileService) {
        this.fileService = fileService;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void init() {
        readSocksStoreFromFile();
        readOperationsFromFile();
    }

    @Override
    public boolean acceptSocks(SocksDTO socksDTO) {
        Socks socks = buildSocksFromDTO(socksDTO);
        if (socksStore.containsKey(socks)) {
            int storeQuantity = socksStore.get(socks);
            socksStore.put(socks, storeQuantity + socksDTO.getQuantity());
        } else {
            socksStore.put(socks, socksDTO.getQuantity());
        }
        saveSocksStoreToFile();
        Operation operation = Operation.buildFromSockDTO(OPERATION_TYPE.ACCEPT, socksDTO);
        operationList.add(operation);
        saveOperationsToFile();
        return true;
    }

    @Override
    public boolean writeOffSocks(SocksDTO socksDTO) {
        if (decreaseSocks(socksDTO)) {
            saveSocksStoreToFile();
            Operation operation = Operation.buildFromSockDTO(OPERATION_TYPE.WRITEOFF, socksDTO);
            operationList.add(operation);
            saveOperationsToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean releaseSocks(SocksDTO socksDTO) {
        if (decreaseSocks(socksDTO)) {
            saveSocksStoreToFile();
            Operation operation = Operation.buildFromSockDTO(OPERATION_TYPE.RELEASE, socksDTO);
            operationList.add(operation);
            saveOperationsToFile();
            return true;
        }
        return false;
    }

    private boolean decreaseSocks(SocksDTO socksDTO) {
        Socks socks = buildSocksFromDTO(socksDTO);
        if (socksStore.containsKey(socks)) {
            int storeQuantity = socksStore.get(socks);
            int releaseQuantity = socksDTO.getQuantity();
            if (releaseQuantity > storeQuantity) {
                return false;
            }
            socksStore.put(socks, storeQuantity - releaseQuantity);
            saveSocksStoreToFile();
            return true;
        }
        return false;
    }

    @Override
    public int getQuantity(String color, String size, Integer cottonMin, Integer cottonMax) {
        SocksPattern socksPattern = SocksPattern.valueOf(color, size, cottonMin, cottonMax);
        int count = 0;
        for (Map.Entry<Socks, Integer> entry : socksStore.entrySet()) {
            if (entry.getKey().isMatch(socksPattern)) {
                count += entry.getValue();
            }
        }
        return count;
    }

    private void saveSocksStoreToFile() {
        List<SocksDTO> socksList = socksStore.entrySet()
                .stream()
                .map((entry)  -> buildFromSocks(entry.getKey(), entry.getValue()))
                .toList();
        try {
            String json = mapper.writeValueAsString(socksList);
            fileService.saveSocksStoreToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readSocksStoreFromFile() {
        String json = fileService.readWarehouseFromFile();
        try {
            if (!StringUtils.isEmpty(json)) {
                List<SocksDTO> socksList = mapper.readValue(json, new TypeReference<>() {
                });
                socksStore.clear();
                socksStore = socksList.stream()
                        .collect(Collectors.toMap(SocksDTO::createSocks, SocksDTO::getQuantity));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void saveOperationsToFile() {
        try {
            String json = mapper.writeValueAsString(operationList);
            fileService.saveOperationsToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readOperationsFromFile() {
        String json = fileService.readOperationsFromFile();
        try {
            if (!StringUtils.isEmpty(json)) {
                operationList.clear();
                operationList = mapper.readValue(json, new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Socks buildSocksFromDTO(SocksDTO socksDTO) {
        return new Socks(socksDTO.getColor(),socksDTO.getSize(), socksDTO.getComposition());
    }
}
