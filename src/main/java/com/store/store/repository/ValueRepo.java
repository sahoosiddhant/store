package com.store.store.repository;

import com.store.store.entity.StoreEntity;
import com.store.store.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValueRepo extends JpaRepository<Value,Long> {
}
