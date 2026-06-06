package com.orbitbook.booking.hateoas;

import com.orbitbook.booking.controller.BookingController;
import com.orbitbook.booking.dto.booking.BookingResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BookingModelAssembler
        implements RepresentationModelAssembler<
        BookingResponseDTO,
        EntityModel<BookingResponseDTO>> {

    @Override
    public EntityModel<BookingResponseDTO> toModel(
            BookingResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(
                        methodOn(BookingController.class)
                                .findById(
                                        dto.getIdBookings()
                                )
                ).withSelfRel(),

                linkTo(
                        methodOn(BookingController.class)
                                .findAll()
                ).withRel("bookings"),

                linkTo(
                        methodOn(BookingController.class)
                                .findByUser(
                                        dto.getUserId()
                                )
                ).withRel("user-bookings")
        );
    }
}