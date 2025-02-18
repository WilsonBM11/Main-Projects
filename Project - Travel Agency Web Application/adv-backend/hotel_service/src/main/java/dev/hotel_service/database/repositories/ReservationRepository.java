package dev.hotel_service.database.repositories;

import dev.hotel_service.database.entity.RoomReservationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<RoomReservationEntity, UUID> {
}
