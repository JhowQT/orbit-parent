package com.orbitbook.booking.mapper;

import com.orbitbook.booking.dto.booking.BookingCreateDTO;
import com.orbitbook.booking.dto.booking.BookingResponseDTO;
import com.orbitbook.booking.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponseDTO toResponseDTO(
            Booking entity) {

        if (entity == null) {
            return null;
        }

        return BookingResponseDTO.builder()
                .idBookings(entity.getIdBookings())
                .departureDate(entity.getDepartureDate())
                .returnDate(entity.getReturnDate())
                .totalPrice(entity.getTotalPrice())
                .numPassengers(entity.getNumPassengers())
                .createdAt(entity.getCreatedAt())
                .userId(entity.getUserId())
                .aiRecommendationId(
                        entity.getAiRecommendationId()
                )
                .destinationId(
                        entity.getDestination()
                                .getIdDestinations()
                )
                .destinationName(
                        entity.getDestination()
                                .getName()
                )
                .bookingStatusId(
                        entity.getBookingStatus()
                                .getIdBookingStatuses()
                )
                .bookingStatusName(
                        entity.getBookingStatus()
                                .getName()
                )
                .build();
    }

    public Booking toEntity(
            BookingCreateDTO dto) {

        if (dto == null) {
            return null;
        }

        return Booking.builder()
                .departureDate(dto.getDepartureDate())
                .returnDate(dto.getReturnDate())
                .numPassengers(dto.getNumPassengers())
                .userId(dto.getUserId())
                .aiRecommendationId(
                        dto.getAiRecommendationId()
                )
                .build();
    }
}