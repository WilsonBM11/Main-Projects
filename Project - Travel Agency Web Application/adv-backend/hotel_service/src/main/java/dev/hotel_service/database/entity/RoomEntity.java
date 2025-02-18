package dev.hotel_service.database.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @Column(name = "room_id")
    private UUID roomId;

    public RoomEntity() {
        roomId = UUID.randomUUID();
    }

    @Column(name = "room_number")
    private Integer roomNumber;

    @Column(name = "details")
    private String details;

    @Column(name = "price")
    private Float price;

    @Column(name = "available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelId;

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public HotelEntity getHotelId() {
        return hotelId;
    }

    public void setHotelId(HotelEntity hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public String toString() {
        return "RoomEntity{" +
                "roomId=" + roomId +
                ", roomNumber=" + roomNumber +
                ", details='" + details + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", hotelId=" + hotelId +
                '}';
    }
}
