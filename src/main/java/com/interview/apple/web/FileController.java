package com.interview.apple.web;

import com.interview.apple.config.Constants;
import com.interview.apple.domain.model.FileStore;
import com.interview.apple.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping(FileController.BASE_URL)
public class FileController {
    public static final String BASE_URL = "/storage/documents";

    private final StorageService storageService;

    @PostMapping("/")
    public ResponseEntity<String> saveFile(@RequestParam("file") MultipartFile file) {

        FileStore dbFile = storageService.save(file);

        String createdUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(BASE_URL + "/")
                .path(dbFile.getId())
                .toUriString();

        return ResponseEntity.created(URI.create(createdUrl))
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .contentLength(Constants.ID_LENGTH)
                .body(dbFile.getId());
    }

    @PutMapping("/")
    public ResponseEntity<String> updateFile(@RequestParam String id, @RequestParam("file") MultipartFile file) {

        FileStore dbFile = storageService.update(id, file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .contentLength(Constants.ID_LENGTH)
                .body(dbFile.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        FileStore file = storageService.getFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getData()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeFile(@PathVariable String id) {
        FileStore dbFile = storageService.getFile(id);
        storageService.removeFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .contentLength(Constants.ID_LENGTH)
                .body(dbFile.getId());
    }

}

