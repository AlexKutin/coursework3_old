package pro.sky.kutin.coursework3.services.impl;

import org.springframework.stereotype.Service;
import pro.sky.kutin.coursework3.dto.SocksDTO;
import pro.sky.kutin.coursework3.model.Socks;
import pro.sky.kutin.coursework3.model.SocksPattern;
import pro.sky.kutin.coursework3.services.SocksService;

import java.util.HashMap;
import java.util.Map;

@Service
public class SocksServiceImpl implements SocksService {
    private static final Map<Socks, Integer> socksStore = new HashMap<>();

    @Override
    public boolean registerSocks(SocksDTO socksDTO) {
        Socks socks = buildSocksFromDTO(socksDTO);
        if (socksStore.containsKey(socks)) {
            int storeQuantity = socksStore.get(socks);
            socksStore.put(socks, storeQuantity + socksDTO.getQuantity());
        } else {
            socksStore.put(socks, socksDTO.getQuantity());
        }
        return true;
    }

    @Override
    public boolean releaseSocks(SocksDTO socksDTO) {
        Socks socks = buildSocksFromDTO(socksDTO);
        if (socksStore.containsKey(socks)) {
            int storeQuantity = socksStore.get(socks);
            int releaseQuantity = socksDTO.getQuantity();
            if (releaseQuantity > storeQuantity) {
                return false;
            }
            socksStore.put(socks, storeQuantity - releaseQuantity);
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

    private Socks buildSocksFromDTO(SocksDTO socksDTO) {
        return new Socks(socksDTO.getColor(),socksDTO.getSize(), socksDTO.getComposition());
    }
}
