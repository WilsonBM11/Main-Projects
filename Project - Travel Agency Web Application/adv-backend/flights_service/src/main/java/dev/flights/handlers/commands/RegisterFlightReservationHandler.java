package dev.flights.handlers.commands;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.documents.FlightSeatDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.handlers.types.PassengerCompanion;
import dev.flights.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RegisterFlightReservationHandler {

    private final FlightRepository repository;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public RegisterFlightReservationHandler(FlightRepository repository, JmsTemplate jmsTemplate) {
        this.repository = repository;
        this.jmsTemplate = jmsTemplate;
    }
    public record Command(

            String flightId,

            FlightSeatDocument.FlightClass flightClass,
            String passengerName,
            String dateOfBirth,

            String passengerEmail,

            String passengerTelephone,

            Optional<List<PassengerCompanion>> companions


    ){}

    public String handle(Command command){

        validateFields(command);

        String userId = SessionContextHolder.getSession().getId();

        Optional<FlightDocument> flight = repository.findById(command.flightId());

        validateFlightExist(flight);

        addPassenger(command, userId, flight);

        try {
            repository.save(flight.get());

            jmsTemplate.convertAndSend("message", messageToEmail(flight.get(), command));
            jmsTemplate.convertAndSend("subject", "Detalles de la reserva");
            jmsTemplate.convertAndSend("toUser", command.passengerEmail());

        }catch (RuntimeException ex){
            //
        }


        return "OK";

    }

    public String messageToEmail(FlightDocument flight, Command command) {
        StringBuilder accompanyingPassengersHtml = new StringBuilder();

        if (command.companions != null && !command.companions.isEmpty()) {
            accompanyingPassengersHtml.append("            <h3>Acompañantes:</h3>")
                    .append("            <ul class='details'>");
            for (PassengerCompanion accomp : command.companions.get()) {
                accompanyingPassengersHtml.append("                <li><b>Nombre del acompañante:</b> ").append(accomp.name()).append("</li>")
                        .append("                <li><b>Fecha de nacimiento:</b> ").append(accomp.dateOfBirth()).append("</li>");
            }
            accompanyingPassengersHtml.append("            </ul>");
        }

        return "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta http-equiv='X-UA-Compatible' content='IE=edge'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Reserva de vuelo</title>" +
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
                "            <h1>EasyTours</h1>"+
                "            <h2>Reserva de vuelo</h2>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Se ha realizado una reserva de vuelo con destino a: " + flight.getDestination() + "</p>" +
                "            <p>Detalles del vuelo:</p>" +
                "            <ul class='details'>" +
                "                <li><b>Aerolinea:</b> " + flight.getAirline() + "</li>" +
                "                <li><b>Salida:</b> " + flight.getDeparture() + "</li>" +
                "                <li><b>Destino:</b> " + flight.getDestination() + "</li>" +
                "                <li><b>Fecha de salida:</b> " + flight.getDepartureTime() + "</li>" +
                "                <li><b>Fecha de llegada:</b> " + flight.getArrivalTime() + "</li>" +
                "            <p>Detalles de la reserva:</p>" +
                "            <ul class='details'>" +
                "                <li><b>Nombre del pasajero:</b> " + command.passengerName() + "</li>" +
                "                <li><b>Fecha de nacimiento:</b> " + command.dateOfBirth() + "</li>" +
                "                <li><b>Email:</b> " + command.passengerEmail() + "</li>" +
                "                <li><b>Teléfono:</b> " + command.passengerTelephone() + "</li>" +
                "                <li><b>Precio:</b> " + flight.getFirstClassPrice() + "</li>" +
                "                <li><b>Clase:</b> " + command.flightClass() + "</li>" +
                "            </ul>" +
                accompanyingPassengersHtml.toString() +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>¡Viaja bien, viaja seguro, viaja con EasyTours!</p>" +
                "            <p>Atentamente,<br>El equipo de EasyTours </p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }


    private void addPassenger(Command command, String userId, Optional<FlightDocument> flight){
        if(command.companions().isPresent()){
            addPassengerAndCompanions(command,userId, flight.get());
        }else{
            addPassenger(command,userId, flight.get());
        }
    }

    private void validateFields(Command command) {
        if (isNullOrEmpty(command.flightId())) {
            throw new BusinessException("Flight ID is required.", 400);
        }
        if (isNullOrEmpty(command.flightClass().toString())) {
            throw new BusinessException("Flight class is required.", 400);
        }
        if (isNullOrEmpty(command.passengerName())) {
            throw new BusinessException("Passenger name is required.", 400);
        }
        if (isNullOrEmpty(command.dateOfBirth())) {
            throw new BusinessException("Date of birth is required.", 400);
        }
        if (isNullOrEmpty(command.passengerEmail())) {
            throw new BusinessException("Email is required.", 400);
        }
        if (isNullOrEmpty(command.passengerTelephone())) {
            throw new BusinessException("Telephone is required.", 400);
        }

    }

    private void validateFlightExist(Optional<FlightDocument> flight){
        if(flight.isPresent() == false){
            throw new BusinessException("This flight does not exist",400);
        }

    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }


    private void addPassengerAndCompanions(Command command, String userId, FlightDocument flight){

        List<FlightSeatDocument> seats = flight.getSeats();

        FlightSeatDocument seat = new FlightSeatDocument();

        seat.setAvailable(false);
        seat.setPassengerId(userId);
        seat.setFlightClass(command.flightClass());
        seat.setSeatId(UUID.randomUUID().toString());
        seat.setPassengerEmail(command.passengerEmail());
        seat.setPassengerName(command.passengerName());
        seat.setPassengerBirthDate(command.dateOfBirth());
        seat.setPassengerTelephone(command.passengerTelephone());
        seat.setState(FlightSeatDocument.FlightState.RESERVED);
        setSeatPrice(seat,flight);
        seats.add(seat);

        decreaseSeats(command.flightClass(), flight);

        for(PassengerCompanion companion: command.companions().get()){
            FlightSeatDocument seatCompanion = new FlightSeatDocument();
            seatCompanion.setState(FlightSeatDocument.FlightState.RESERVED);
            seatCompanion.setAvailable(false);
            seatCompanion.setFlightClass(command.flightClass());
            seatCompanion.setSeatId(UUID.randomUUID().toString());
            seatCompanion.setPassengerId(userId);
            seatCompanion.setPassengerName(companion.name());
            seatCompanion.setPassengerBirthDate(companion.dateOfBirth());
            seatCompanion.setPassengerEmail(command.passengerEmail());
            setSeatPrice(seatCompanion,flight);
            seats.add(seatCompanion);

            decreaseSeats(command.flightClass(), flight);
        }


    }

    private void addPassenger(Command command, String userId, FlightDocument flight){

        List<FlightSeatDocument> seats = flight.getSeats();

        FlightSeatDocument seat = new FlightSeatDocument();
        seat.setAvailable(false);
        seat.setFlightClass(command.flightClass);
        seat.setSeatId(UUID.randomUUID().toString());
        seat.setPassengerId(userId);
        seat.setPassengerEmail(command.passengerEmail());
        seat.setPassengerName(command.passengerName());
        seat.setPassengerBirthDate(command.dateOfBirth());
        seat.setPassengerTelephone(command.passengerTelephone());
        seat.setPassengerEmail(command.passengerEmail());
        setSeatPrice(seat,flight);
        seats.add(seat);
        decreaseSeats(command.flightClass, flight);
        seat.setState(FlightSeatDocument.FlightState.RESERVED);

    }

    private void setSeatPrice(FlightSeatDocument seat, FlightDocument flight){
        if(seat.getFlightClass().toString().equals("FIRST")){
            seat.setPrice(flight.getFirstClassPrice());
        }
        if(seat.getFlightClass().toString().equals("BUSINESS")){
            seat.setPrice(flight.getBusinessClassPrice());
        }
        if(seat.getFlightClass().toString().equals("COMMERCIAL")){
            seat.setPrice(flight.getCommercialClassPrice());
        }
    }


    private void decreaseSeats (FlightSeatDocument.FlightClass flightClass, FlightDocument flight){
        if(flightClass.toString().equals("FIRST")){
            checkMinus(flight.getAvailableFirstClassSeats());
            flight.setAvailableFirstClassSeats(flight.getAvailableFirstClassSeats() - 1);
        }
        if(flightClass.toString().equals("BUSINESS")){
            checkMinus(flight.getAvailableBusinessClassSeats());
            flight.setAvailableBusinessClassSeats(flight.getAvailableBusinessClassSeats() - 1);
        }
        if(flightClass.toString().equals("COMMERCIAL")){
            checkMinus(flight.getAvailableCommercialClassSeats());
            flight.setAvailableCommercialClassSeats(flight.getAvailableCommercialClassSeats() - 1);
        }
    }


    private void checkMinus(int seats){
        if (seats - 1  < 0 ){
            throw new BusinessException("There are not available seats on the requested flight class",400);
        }
    }



}
