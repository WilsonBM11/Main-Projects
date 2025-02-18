package dev.hotel_service.api.types;

import java.util.Date;
import java.util.UUID;

public record ReservationControllerRequest (
    String startDate,
    String endDate,
    String userEmail,
    UUID roomId
){}
