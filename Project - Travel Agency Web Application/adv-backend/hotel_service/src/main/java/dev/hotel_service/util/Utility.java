package dev.hotel_service.util;

import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utility {

    public LocalDate parseDate(String date, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new BusinessException("Invalid date format. Please use dd/MM/yyyy.", ErrorCodes.NOT_FOUND);
        }
    }

    public LocalDate formatterAndValidateStartDates(String startDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate formattedStartDate = parseDate(startDate, formatter);
        return formattedStartDate;
    }

    public LocalDate formatterAndValidateEndDates(String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate formattedEndDate = parseDate(endDate, formatter);
        return formattedEndDate;
    }

    public void validateStartDateNotBeforeToday(LocalDate startDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new BusinessException("The start day can't be before today",ErrorCodes.INVALID_DATA);
        }
    }

    public void validateEndDateNotBeforeStartDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BusinessException("The end date can't be before than the start date",ErrorCodes.INVALID_DATA);
        }
    }
}
