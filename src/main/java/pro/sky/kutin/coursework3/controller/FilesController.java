package pro.sky.kutin.coursework3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.kutin.coursework3.services.FileService;
import pro.sky.kutin.coursework3.services.SocksService;

import java.io.*;

@RestController
@RequestMapping("/files")
@Tag(name = "Файлы", description = "Скачивание/загрузка состояния склада носков и журнала операций")
public class FilesController {
    private final FileService fileService;
    private final SocksService socksService;

    public FilesController(FileService fileService, SocksService socksService) {
        this.fileService = fileService;
        this.socksService = socksService;
    }

    @Operation(
            summary = "Экспорт данных с состоянием склада",
            description = "Скачать файл json с состоянием склада носков на текущий момент"
    )
    @GetMapping("/warehouse/download")
    public ResponseEntity<InputStreamResource> downloadWarehouseStateToFile()  {
        File warehouseStateFile = fileService.getWarehouseDataFile();
        if (warehouseStateFile.exists()) {
            try {
                InputStreamResource inputResource = new InputStreamResource(new FileInputStream(warehouseStateFile));
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(warehouseStateFile.length())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"WarehouseState.json\"")
                        .body(inputResource);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Импорт данных с состоянием склада",
            description = "Загрузить на сервер файл json с состоянием склада носков на текущий момент"
    )
    @PostMapping(value = "/warehouse/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadWarehouseStateFromFile(@RequestParam MultipartFile warehouseUploadFile) {
        fileService.cleanWarehouseDataFile();
        File warehouseStateDataFile = fileService.getWarehouseDataFile();
        try (FileOutputStream fos = new FileOutputStream(warehouseStateDataFile)) {
            IOUtils.copy(warehouseUploadFile.getInputStream(), fos);
            socksService.readSocksStoreFromFile();
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Operation(
            summary = "Экспорт журнала операций склада",
            description = "Скачать файл json с журналом операций учета товаров на складе носков"
    )
    @GetMapping("/operations/download")
    public ResponseEntity<InputStreamResource> downloadOperationsLogToFile()  {
        File operationsLogFile = fileService.getOperationsDataFile();
        if (operationsLogFile.exists()) {
            try {
                InputStreamResource inputResource = new InputStreamResource(new FileInputStream(operationsLogFile));
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(operationsLogFile.length())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"OperationsLog.json\"")
                        .body(inputResource);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(
            summary = "Импорт журнала операций склада",
            description = "Загрузить на сервер файл json с журналом операций учета товаров на складе носков"
    )
    @PostMapping(value = "/operations/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadOperationsLogFromFile(@RequestParam MultipartFile operationsLogUploadFile) {
        fileService.cleanOperationsLogDataFile();
        File operationsLogDataFile = fileService.getOperationsDataFile();
        try (FileOutputStream fos = new FileOutputStream(operationsLogDataFile)) {
            IOUtils.copy(operationsLogUploadFile.getInputStream(), fos);
            socksService.readOperationsFromFile();
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
