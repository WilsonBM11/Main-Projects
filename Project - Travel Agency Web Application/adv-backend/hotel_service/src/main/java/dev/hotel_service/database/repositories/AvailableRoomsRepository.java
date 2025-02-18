package dev.hotel_service.database.repositories;

import dev.hotel_service.database.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvailableRoomsRepository extends JpaRepository<RoomEntity, UUID> {
    List<RoomEntity> findByAvailableTrueAndHotelId_HotelId(UUID hotelId);
}

