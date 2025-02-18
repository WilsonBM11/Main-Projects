package dev.hotel_service.handler.queries;


import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class GetHotelsHomepageHandler {

    @Autowired
    private HotelRepository hotelRepository;

    public List<HotelEntity> handle(){
        List<HotelEntity> responses = hotelRepository.findAll();

        // Mezcla la lista para seleccionar elementos aleatorios
        Collections.shuffle(responses, new Random());

        // Retorna los primeros 8 elementos de la lista mezclada
        return responses.stream().limit(8).collect(Collectors.toList());
    }
}