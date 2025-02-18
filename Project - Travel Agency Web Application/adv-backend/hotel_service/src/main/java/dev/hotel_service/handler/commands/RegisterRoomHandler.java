package dev.hotel_service.handler.commands;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.repositories.AvailableRoomsRepository;
import dev.hotel_service.database.repositories.HotelRepository;
import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RegisterRoomHandler {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AvailableRoomsRepository availableRoomsRepository;

    SessionContextHolder sessionContextHolder = new SessionContextHolder();

    public record Command(
            Integer roomNumber,
            String details,
            Float price,
            UUID hotelId
    ){}

    public void registerRoom(Command command) {
        // Validate user permissions
        Session session = SessionContextHolder.getSession();
        sessionContextHolder.validateUserAdmin(session.roles());

        // Validaciones
        validateRequiredFields(command);
        validatePositivePrice(command.price());
        validateHotel(command.hotelId());
        validateNumRoom(command.hotelId(), command.roomNumber());

        // Crear objeto RoomEntity
        RoomEntity room = new RoomEntity();
        room.setRoomNumber(command.roomNumber());
        room.setDetails(command.details());
        room.setPrice(command.price());
        room.setAvailable(true);
        HotelEntity hotelRoom = getHotelRoom(command.hotelId());
        room.setHotelId(hotelRoom);

        // Guardar habitación en la base de datos
        roomRepository.save(room);
    }


    private void validateRequiredFields(Command command) {
        if ( command.roomNumber() == null || command.details() == null || command.price() == null || command.hotelId() == null) {
            throw new BusinessException("Must complete all room data",ErrorCodes.NULL_DATA);
        }
    }

    private void validateHotel(UUID hotelId) {
        Optional<HotelEntity> hotel= hotelRepository.findById(hotelId);
        if (!hotel.isPresent()) {
            throw new BusinessException("Hotel not found", ErrorCodes.NOT_FOUND);
        }
    }

    private HotelEntity getHotelRoom(UUID hotelId) {
        List<HotelEntity> hotels= hotelRepository.findAll();
        HotelEntity hotelOpt= new HotelEntity();
        int value=0;
        while(value<hotels.size()){
            if(hotels.get(value).getHotelId().equals(hotelId)){
                hotelOpt=hotels.get(value);
            }
            value++;
        }
        return hotelOpt;
    }

    private void validateNumRoom(UUID hotel_id, Integer roomNumber) {
        List<RoomEntity> rooms = availableRoomsRepository.findByAvailableTrueAndHotelId_HotelId(hotel_id);
        for (RoomEntity room : rooms) {
            if (room.getHotelId() != null && room.getRoomNumber() != null) {
                if (room.getRoomNumber().equals(roomNumber)) {
                    throw new BusinessException("This room number already exists in this hotel", ErrorCodes.INVALID_DATA);
                }
            }
        }
    }

    private void validatePositivePrice(Float price) {
        if (price <= 0) {
            throw new BusinessException("Price must be a positive value",ErrorCodes.INVALID_DATA);
        }
    }
}