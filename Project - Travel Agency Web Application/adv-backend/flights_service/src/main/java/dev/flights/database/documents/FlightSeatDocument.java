package dev.flights.database.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class FlightSeatDocument {

    private String seatId;
    private FlightClass flightClass;
    private double price;
    private boolean available;
    private String luggage;
    private String services;

    private String passengerName;
    private String passengerId;

    private String passengerBirthDate;

    private String passengerTelephone;

    private String passengerEmail;

    private FlightState state;
    public enum FlightClass {
        FIRST, BUSINESS, COMMERCIAL
    }

    public enum FlightState {
        RESERVED, CANCELLED
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerBirthDate() {
        return passengerBirthDate;
    }

    public void setPassengerBirthDate(String passengerBirthDate) {
        this.passengerBirthDate = passengerBirthDate;
    }

    public String getPassengerTelephone() {
        return passengerTelephone;
    }

    public void setPassengerTelephone(String passengerTelephone) {
        this.passengerTelephone = passengerTelephone;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public FlightState getState() {
        return state;
    }

    public void setState(FlightState state) {
        this.state = state;
    }

    public String getSeatId() {
        return seatId;
    }

    public FlightSeatDocument setSeatId(String seatId) {
        this.seatId = seatId;
        return this;
    }

    public FlightClass getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClass flightClass) {
        this.flightClass = flightClass;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getLuggage() {
        return luggage;
    }

    public void setLuggage(String luggage) {
        this.luggage = luggage;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }
}