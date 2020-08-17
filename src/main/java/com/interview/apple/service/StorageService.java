package com.interview.apple.service;

import com.interview.apple.domain.model.FileStore;
import com.interview.apple.domain.repository.FileStoreRepository;
import com.interview.apple.exception.NoContentException;
import com.interview.apple.exception.NoFileException;
import com.interview.apple.exception.SaveFileException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class StorageService {

    private final FileStoreRepository repository;

    public FileStore saveFile(FileStore dbFile) {
        try {
            return repository.save(dbFile);
        } catch (DataAccessException e) {
            throw new SaveFileException("Error saving file " + dbFile.getFileName() + " !", e);
        }
    }

    public FileStore save(MultipartFile file) {
        FileStore dbFile = new FileStore(getFileName(file), file.getContentType(), getFileData(file));
        return this.saveFile(dbFile);
    }

    public FileStore update(String id, MultipartFile file) {
        FileStore dbFile;
        try {
            dbFile = this.getFile(id);
        } catch (NoFileException e) {
            throw new NoContentException(e);
        }
        dbFile.setFileName(getFileName(file));
        dbFile.setFileType(file.getContentType());
        dbFile.setData(getFileData(file));
        return this.saveFile(dbFile);
    }

    private String getFileName(MultipartFile file) {
        return StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    }

    private byte[] getFileData(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new SaveFileException("Error getting data from file " + file.getOriginalFilename() + " !", e);
        }
    }

    public FileStore getFile(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoFileException("File with id = " + id + " not found !"));
    }

    public void removeFile(String id) {
        repository.deleteById(id);
    }
}
