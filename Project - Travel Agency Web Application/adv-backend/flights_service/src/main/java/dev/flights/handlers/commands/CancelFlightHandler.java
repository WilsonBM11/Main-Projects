package dev.flights.handlers.commands;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.documents.FlightSeatDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CancelFlightHandler {

    private final FlightRepository flightRepository;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public CancelFlightHandler(FlightRepository flightRepository, JmsTemplate jmsTemplate) {
        this.flightRepository = flightRepository;
        this.jmsTemplate = jmsTemplate;
    }

    public record Command(
            String id
    ) {
    }

    public void cancelFlight(Command command){
        Session session = SessionContextHolder.getSession();
        String passengerId = session.getId();

        FlightDocument flight = flightRepository.findFlightBySeatId(command.id)
                        .orElseThrow(() -> new BusinessException("Flight not found", 403));

        FlightSeatDocument seatToCancel = flight.getSeats().stream()
                .filter(seat -> seat.getSeatId().equals(command.id()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Seat not found", 403));

        assert passengerId != null;
        validateSeatOwnership(passengerId, seatToCancel);
        try {
            flightRepository.save(flight);
            double refund = calculateRefund(seatToCancel.getPrice(), flight.getDepartureTime());
            jmsTemplate.convertAndSend("message", messageToEmail(flight, seatToCancel, refund));
            jmsTemplate.convertAndSend("subject", "Cancelacion de vuelo exitosa");
            jmsTemplate.convertAndSend("toUser", seatToCancel.getPassengerEmail());

        }catch (RuntimeException ex){
            //
        }
    }

    private void validateSeatOwnership(String passengerId, FlightSeatDocument seat){
        if(!passengerId.equals(seat.getPassengerId())){
            throw new BusinessException("Seat not reserved by the current user", 403);
        }
        seat.setState(FlightSeatDocument.FlightState.CANCELLED);
        seat.setAvailable(true);
    }

    private String messageToEmail(FlightDocument flight, FlightSeatDocument seat, double refund){
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
                "            <h2>Su vuelo ha sido cancelado</h2>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Estimado/a cliente,</p>" +
                "            <p>Le confirmamos que su vuelo con destino a <b>" + flight.getDestination() + "</b> ha sido cancelado a petición suya. A continuación, encontrará los detalles del vuelo:</p>" +
                "            <ul class='details'>" +
                "                <li><b>Nombre del vuelo:</b> " + flight.getAirline() + "</li>" +
                "                <li><b>Fecha de salida:</b> " + flight.getDepartureTime() + "</li>" +
                "                <li><b>Fecha de llegada:</b> " + flight.getArrivalTime() + "</li>" +
                "                <li><b>Origen:</b> " + flight.getDeparture() + "</li>" +
                "                <li><b>Destino:</b> " + flight.getDestination() + "</li>" +
                "                <li><b>Asiento:</b> " + seat.getFlightClass() + "</li>" +
                "                <li><b>Precio: $</b> " + seat.getPrice() + "</li>" +
                "            </ul>" +
                "            <p>Se le ha reembolsado la cantidad de <b>$" + refund + "</b> en su tarjeta de crédito.</p>" +
                "            <p>Gracias por informarnos de su cancelación. Si tiene alguna pregunta o necesita asistencia adicional, no dude en contactarnos.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>¡Viaja bien, viaja seguro, viaja con EasyTours!</p>" +
                "            <p>Atentamente,<br>El equipo de EasyTours</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    private double calculateRefund(double price, Date departureTime){
        Date now = new Date();
        long diff = departureTime.getTime() - now.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if(diffDays > 7){
            return price;
        }else if(diffDays > 3){
            return (int) (price * 0.75);
        }else if(diffDays > 1){
            return (int) (price * 0.5);
        }else{
            return (int) (price * 0.25);
        }
    }

}
