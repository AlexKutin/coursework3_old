package pro.sky.kutin.coursework3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.kutin.coursework3.dto.SocksDTO;
import pro.sky.kutin.coursework3.model.SOCKS_COLOR;
import pro.sky.kutin.coursework3.model.SOCKS_SIZE;
import pro.sky.kutin.coursework3.services.SocksService;

@RestController("/api")
@Tag(name = "Склад интернет-магазина носков", description = "CRUD-операции и другие эндпоинты для автоматизации учета товаров")
public class SocksController {
    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    // ### **POST /api/socks**
    // Регистрирует приход товара на склад
    @Operation(
            summary = "Регистрирует приход товара на склад",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Параметры носков и их количество",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SocksDTO.class)
            )
    )
    @PostMapping("/socks")
    public ResponseEntity<Void> acceptSocksToWarehouse(@RequestBody SocksDTO socksDTO) {
        int quantity = socksDTO.getQuantity();
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (socksService.acceptSocks(socksDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // ### **PUT /api/socks**
    // Регистрирует отпуск носков со склада.
    // Здесь параметры и результаты аналогичные,
    // но общее количество носков указанного цвета и состава не увеличивается, а уменьшается.
    @Operation(
            summary = "Регистрирует отпуск носков со склада",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Параметры носков и их количество",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SocksDTO.class)
            )
    )
    @PutMapping("/socks")
    public ResponseEntity<Void> releaseSocksFromWarehouse(@RequestBody SocksDTO socksDTO) {
        int quantity = socksDTO.getQuantity();
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (socksService.releaseSocks(socksDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // ### **GET /api/socks**
    // Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса
    @Operation(
            summary = "Получение количества носков на склада",
            description = "Возвращает общее количество носков на складе, соответствующих критериям запроса"
    )
    @Parameter(name = "color", description = "Цвет носков", schema = @Schema(implementation = SOCKS_COLOR.class), required = false)
    @Parameter(name = "size", description = "Размер", schema = @Schema(implementation = SOCKS_SIZE.class), required = false)
    @Parameter(name = "cottonMin", description = "минимальное значение хлопка в товаре", required = false)
    @Parameter(name = "cottonMax", description = "максимальное значение хлопка в товаре", required = false)
    @GetMapping("/socks")
    public ResponseEntity<String> getQuantitySocksByPattern(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Integer cottonMin,
            @RequestParam(required = false) Integer cottonMax) {

        int quantity;
        try {
            quantity = socksService.getQuantity(color, size, cottonMin, cottonMax);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(String.valueOf(quantity));
    }

    // Регистрирует списание испорченных (бракованных) носков
    @Operation(
            summary = "Регистрирует списание испорченных (бракованных) носков",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Параметры носков и их количество",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SocksDTO.class)
            )
    )
    @DeleteMapping("/socks")
    public ResponseEntity<Void> writeOffDefectiveSocks(@RequestBody SocksDTO socksDTO) {
        int quantity = socksDTO.getQuantity();
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (socksService.writeOffSocks(socksDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
