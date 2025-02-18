package dev.hotel_service.database.repositories;

import dev.hotel_service.database.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NameHotelsRepository extends JpaRepository<HotelEntity, UUID> {
    List<HotelEntity> findByNameContainingIgnoreCase(String name);
}
