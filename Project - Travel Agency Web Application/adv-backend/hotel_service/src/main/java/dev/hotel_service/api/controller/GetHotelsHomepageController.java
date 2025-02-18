package dev.hotel_service.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.handler.queries.GetHotelsHomepageHandler;

import java.util.List;

@RestController
public class GetHotelsHomepageController {

    @Autowired
    GetHotelsHomepageHandler handler;

    @GetMapping("/api/private/hotels/home")
    public List<HotelEntity> getHotelsHomepage() {
        return handler.handle();
    }
}
