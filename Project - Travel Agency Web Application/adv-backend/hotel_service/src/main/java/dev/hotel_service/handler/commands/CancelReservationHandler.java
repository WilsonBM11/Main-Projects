package dev.hotel_service.handler.commands;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.ReservationRepository;
import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.util.EmailUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CancelReservationHandler {

    @Autowired
    public ReservationRepository reservationRepository;

    @Autowired
    public RoomRepository roomRepository;

    @Autowired
    private JmsTemplate jmsTemplate;


    @Autowired
    public CancelReservationHandler(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate=jmsTemplate;
    }

    EmailUtility emailUtility= new EmailUtility();

    public void cancelReservation(UUID reservationId){
        //validaciones
        validateRequiredFields(reservationId);

        RoomReservationEntity reservation = new RoomReservationEntity();
        reservation = getReservation(reservationId);
        reservation.setCanceled(true);
        reservationRepository.save(reservation);

        RoomEntity room = new RoomEntity();
        room = reservation.getRoomId();
        room.setAvailable(true);
        roomRepository.save(room);

        // Enviar el objeto
        jmsTemplate.convertAndSend("message", emailUtility.cancelationMessageToEmail(reservation, room, getHotel(room.getRoomId())));
        jmsTemplate.convertAndSend("subject", "Detalles de la cancelación");
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

    private RoomReservationEntity getReservation(UUID reservationId) {
        List<RoomReservationEntity> reservation= reservationRepository.findAll();
        RoomReservationEntity reservationOpt= new RoomReservationEntity();
        int value=0;
        if(reservation.isEmpty()){
            throw new BusinessException("Reservation not found", ErrorCodes.NOT_FOUND);
        }else{
            while(value<reservation.size()){
                if(reservation.get(value).getReservationId().equals(reservationId)){
                    reservationOpt=reservation.get(value);
                }
                value++;
            }
        }

        if(reservationOpt.getCanceled().booleanValue()){
            throw new BusinessException("The reservation is already canceled", ErrorCodes.NOT_FOUND);
        }
        return reservationOpt;
    }

    private void validateRequiredFields(UUID reservationId) {
        if (reservationId == null) {
            throw new BusinessException("The reservation ID is null", ErrorCodes.NULL_DATA);
        }
    }

}
