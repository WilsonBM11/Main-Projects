package dev.hotel_service.database.repositories;

import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationsByUserRepository extends JpaRepository<RoomReservationEntity, UUID> {
    List<RoomReservationEntity> findReservationsByUserId (UUID userId);
}
