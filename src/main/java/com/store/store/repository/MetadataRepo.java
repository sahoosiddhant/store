package com.store.store.repository;

import com.store.store.entity.Metadata;
import com.store.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataRepo extends JpaRepository<Metadata,Long> {
}
