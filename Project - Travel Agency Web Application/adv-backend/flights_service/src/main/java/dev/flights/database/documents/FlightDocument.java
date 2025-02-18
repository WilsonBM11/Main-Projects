package dev.flights.database.documents;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.util.Date;
import java.util.List;

@Document(collection = "flights")
public class FlightDocument {
    @MongoId
    private String id;
    private String airline;
    private String departure;
    private String destination;
    private Date departureTime;
    private Date arrivalTime;
    private int firstClassSeats;
    private int businessClassSeats;
    private int commercialClassSeats;
    private int availableFirstClassSeats;
    private int availableBusinessClassSeats;
    private int availableCommercialClassSeats;
    private double firstClassPrice;
    private double businessClassPrice;
    private double commercialClassPrice;
    private List<FlightSeatDocument> seats;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getFirstClassSeats() {
        return firstClassSeats;
    }

    public void setFirstClassSeats(int firstClassSeats) {
        this.firstClassSeats = firstClassSeats;
    }

    public int getBusinessClassSeats() {
        return businessClassSeats;
    }

    public void setBusinessClassSeats(int businessClassSeats) {
        this.businessClassSeats = businessClassSeats;
    }

    public int getCommercialClassSeats() {
        return commercialClassSeats;
    }

    public void setCommercialClassSeats(int commercialClassSeats) {
        this.commercialClassSeats = commercialClassSeats;
    }

    public List<FlightSeatDocument> getSeats() {
        return seats;
    }

    public void setSeats(List<FlightSeatDocument> seats) {
        this.seats = seats;
    }

    public int getAvailableFirstClassSeats() {
        return availableFirstClassSeats;
    }

    public void setAvailableFirstClassSeats(int availableFirstClassSeats) {
        this.availableFirstClassSeats = availableFirstClassSeats;
    }

    public int getAvailableBusinessClassSeats() {
        return availableBusinessClassSeats;
    }

    public void setAvailableBusinessClassSeats(int availableBusinessClassSeats) {
        this.availableBusinessClassSeats = availableBusinessClassSeats;
    }

    public int getAvailableCommercialClassSeats() {
        return availableCommercialClassSeats;
    }

    public void setAvailableCommercialClassSeats(int availableCommercialClassSeats) {
        this.availableCommercialClassSeats = availableCommercialClassSeats;
    }

    public double getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(double firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    public double getBusinessClassPrice() {
        return businessClassPrice;
    }

    public void setBusinessClassPrice(double businessClassPrice) {
        this.businessClassPrice = businessClassPrice;
    }

    public double getCommercialClassPrice() {
        return commercialClassPrice;
    }

    public void setCommercialClassPrice(double commercialClassPrice) {
        this.commercialClassPrice = commercialClassPrice;
    }
}