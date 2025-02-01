package com.store.store.repository;

import com.store.store.entity.Message;
import com.store.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message,Long> {
}
