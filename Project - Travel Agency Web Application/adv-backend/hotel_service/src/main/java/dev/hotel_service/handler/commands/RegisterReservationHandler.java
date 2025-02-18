package dev.hotel_service.handler.commands;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.ReservationRepository;
import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.util.EmailUtility;
import dev.hotel_service.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import dev.hotel_service.session.SessionContextHolder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class RegisterReservationHandler {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    SessionContextHolder sessionContextHolder = new SessionContextHolder();

    Utility util = new Utility();

    @Autowired
    private JmsTemplate jmsTemplate;


    @Autowired
    public RegisterReservationHandler(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate=jmsTemplate;
    }

    EmailUtility emailUtility = new EmailUtility();

    public record Command(
            String startDate,
            String endDate,
            String userEmail,
            UUID roomId
    ){}

    public void registerReservation(RegisterReservationHandler.Command command) {
        // Validaciones
        validateRequiredFields(command);
        LocalDate startDate = util.formatterAndValidateStartDates(command.startDate());
        LocalDate endDate = util.formatterAndValidateEndDates(command.endDate());
        util.validateStartDateNotBeforeToday(startDate);
        util.validateEndDateNotBeforeStartDate(startDate, endDate);

        // Crear objeto RoomEntity
        RoomReservationEntity reservation = new RoomReservationEntity();
        reservation.setStartDate(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservation.setEndDate(Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservation.setCanceled(false);
        RoomEntity reservationRoom = getReservationRoom(command.roomId());
        reservation.setRoomId(reservationRoom);
        reservation.setUserEmail(command.userEmail());
        reservation.setUserId(sessionContextHolder.getActualUser());

        // Guardar habitación en la base de datos
        reservationRepository.save(reservation);
        reservationRoom.setAvailable(false);
        roomRepository.save(reservationRoom);

        //enviar correo
        jmsTemplate.convertAndSend("message", emailUtility.reservationMessageToEmail(reservation, reservationRoom, getHotel(reservationRoom.getRoomId())));
        jmsTemplate.convertAndSend("subject", "Detalles de la reserva");
        jmsTemplate.convertAndSend("toUser", reservation.getUserEmail());

    }

    private HotelEntity getHotel(UUID roomId) {
        List<RoomEntity> room= roomRepository.findAll();
        RoomEntity roomOpt= new RoomEntity();
        HotelEntity hotelOpt= new HotelEntity();
        int value=0;
        if(room.isEmpty()){
            throw new BusinessException("Room not found", 402);
        }else{
            while(value<room.size()){
                if(room.get(value).getRoomId().equals(roomId)){
                    roomOpt=room.get(value);
                    hotelOpt = roomOpt.getHotelId();
                }
                value++;
            }
        }

        return hotelOpt;
    }

    private RoomEntity getReservationRoom(UUID roomId) {
        List<RoomEntity> room= roomRepository.findAll();
        RoomEntity roomOpt= new RoomEntity();
        int value=0;
        if(room.isEmpty()){
            throw new BusinessException("Room not found", ErrorCodes.NOT_FOUND);
        }else{
            while(value<room.size()){
                if(room.get(value).getRoomId().equals(roomId)){
                    roomOpt=room.get(value);
                }
                value++;
            }
        }

        if(roomOpt.getAvailable() == false){
            throw new BusinessException("The room is unavailable", ErrorCodes.NOT_FOUND);
        }
        return roomOpt;
    }

    private void validateRequiredFields(RegisterReservationHandler.Command command) {
        if ( command.startDate() == null || command.endDate() == null || command.roomId() == null || command.userEmail() == null) {
            throw new BusinessException("Must complete all room data",ErrorCodes.NULL_DATA);
        }
    }
}
