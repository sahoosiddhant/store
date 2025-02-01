package com.store.store.repository;

import com.store.store.entity.Destination;
import com.store.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepo extends JpaRepository<Destination,Long> {
}
