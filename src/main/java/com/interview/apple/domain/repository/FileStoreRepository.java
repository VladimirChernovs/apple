package com.interview.apple.domain.repository;

import com.interview.apple.domain.model.FileStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStoreRepository extends JpaRepository<FileStore, String> {

}
