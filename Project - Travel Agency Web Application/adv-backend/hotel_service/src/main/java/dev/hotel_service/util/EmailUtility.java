package dev.hotel_service.util;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import dev.hotel_service.exceptions.BusinessException;

import java.util.List;
import java.util.UUID;

public class EmailUtility {

    @Autowired
    RoomRepository roomRepository;

    //Envío de correo de reservas
    public String reservationMessageToEmail(RoomReservationEntity reservation, RoomEntity room, HotelEntity hotel){

        return  "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "        }" +
                "        .container {" +
                "            width: 80%;" +
                "            margin: auto;" +
                "            background-color: #fff;" +
                "            padding: 20px;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        .header {" +
                "            text-align: center;" +
                "            border-bottom: 1px solid #ddd;" +
                "            padding-bottom: 10px;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .header h1 {" +
                "            color: #5cb85c;" +
                "        }" +
                "        .header h2 {" +
                "            color: #d9534f;" +
                "        }" +
                "        .content p {" +
                "            font-size: 1.1em;" +
                "        }" +
                "        .details {" +
                "            list-style-type: none;" +
                "            padding: 0;" +
                "        }" +
                "        .details li {" +
                "            margin: 10px 0;" +
                "        }" +
                "        .details b {" +
                "            display: inline-block;" +
                "            width: 150px;" +
                "        }" +
                "        .footer {" +
                "            text-align: center;" +
                "            font-size: 0.9em;" +
                "            color: #777;" +
                "            margin-top: 20px;" +
                "            border-top: 1px solid #ddd;" +
                "            padding-top: 10px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>EasyTours</h1>" +
                "            <h2>Su habitación ha sido reservada</h2>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Estimado/a cliente,</p>" +
                "            <p>Gracias por reservar con nosotros. Le confirmamos que su habitación ha sido reservada exitosamente. A continuación, encontrará los detalles de la reserva:</p>" +
                "            <ul class='details'>" +
                "                <li><b>Nombre del hotel:</b> " + hotel.getName() + "</li>" +
                "                <li><b>Teléfono del hotel:</b> " + hotel.getPhoneNumber() + "</li>" +
                "                <li><b>Correo del hotel:</b> " + hotel.getEmail() + "</li>"  +
                "                <li><b>Dirección del hotel:</b> " + hotel.getAddress() + "</li>" +
                "                <li><b>Fecha de entrada:</b> " + reservation.getStartDate() + "</li>" +
                "                <li><b>Fecha de salida:</b> " + reservation.getEndDate() + "</li>" +
                "            </ul>" +
                "           <h3>Datos de la habitación:</h3>" +
                "           <ul class='details'>" +
                "               <li><b>Número:</b> " + room.getRoomNumber() + "</li>" +
                "               <li><b>Precio: $</b> " + room.getPrice() + "</li>" +
                "               <li><b>Detalles:</b> " + room.getDetails() + "</li>" +
                "           </ul>" +
                "            <p>Si desea cancelar dirígase a la página web.</p>" +
                "            <p>Si tiene alguna pregunta o necesita asistencia adicional, no dude en contactarnos.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>¡Viaja bien, viaja seguro, viaja con EasyTours!</p>" +
                "            <p>Atentamente,<br>El equipo de EasyTours </p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    //Envío de correo cancelaciones
    public String cancelationMessageToEmail(RoomReservationEntity reservation, RoomEntity room, HotelEntity hotel){

        return "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "        }" +
                "        .container {" +
                "            width: 80%;" +
                "            margin: auto;" +
                "            background-color: #fff;" +
                "            padding: 20px;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        .header {" +
                "            text-align: center;" +
                "            border-bottom: 1px solid #ddd;" +
                "            padding-bottom: 10px;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .header h1 {" +
                "            color: #5cb85c;" +
                "        }" +
                "        .header h2 {" +
                "            color: #d9534f;" +
                "        }" +
                "        .content p {" +
                "            font-size: 1.1em;" +
                "        }" +
                "        .details {" +
                "            list-style-type: none;" +
                "            padding: 0;" +
                "        }" +
                "        .details li {" +
                "            margin: 10px 0;" +
                "        }" +
                "        .details b {" +
                "            display: inline-block;" +
                "            width: 150px;" +
                "        }" +
                "        .footer {" +
                "            text-align: center;" +
                "            font-size: 0.9em;" +
                "            color: #777;" +
                "            margin-top: 20px;" +
                "            border-top: 1px solid #ddd;" +
                "            padding-top: 10px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>EasyTours</h1>" +
                "            <h2>Su reserva ha sido cancelada</h2>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Estimado/a cliente,</p>" +
                "            <p>Le confirmamos que su reserva en el <b>" + hotel.getName() + "</b> ha sido cancelada a petición suya. A continuación, encontrará los detalles de la reserva:</p>" +
                "            <ul class='details'>" +
                "                <li><b>Nombre del hotel:</b> " + hotel.getName() + "</li>" +
                "                <li><b>Teléfono del hotel:</b> " + hotel.getPhoneNumber() + "</li>" +
                "                <li><b>Correo del hotel:</b> " + hotel.getEmail() + "</li>"  +
                "                <li><b>Dirección del hotel:</b> " + hotel.getAddress() + "</li>" +
                "                <li><b>Fecha de entrada:</b> " + reservation.getStartDate() + "</li>" +
                "                <li><b>Fecha de salida:</b> " + reservation.getEndDate() + "</li>" +
                "            </ul>" +
                "           <h3>Datos de la habitación:</h3>" +
                "           <ul class='details'>" +
                "               <li><b>Número:</b> " + room.getRoomNumber() + "</li>" +
                "               <li><b>Precio: $</b> " + room.getPrice() + "</li>" +
                "               <li><b>Detalles:</b> " + room.getDetails() + "</li>" +
                "           </ul>" +
                "            <p>Se le ha reembolsado la cantidad de <b>$" + room.getPrice() + "</b> en su tarjeta de crédito.</p>" +
                "            <p>Si tiene alguna pregunta o necesita asistencia adicional, no dude en contactarnos.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>¡Viaja bien, viaja seguro, viaja con EasyTours!</p>" +
                "            <p>Atentamente,<br>El equipo de EasyTours </p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

}


