package com.orbitbook.booking.hateoas;

import com.orbitbook.booking.controller.ReviewController;
import com.orbitbook.booking.dto.review.ReviewResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReviewModelAssembler
        implements RepresentationModelAssembler<
        ReviewResponseDTO,
        EntityModel<ReviewResponseDTO>> {

    @Override
    public EntityModel<ReviewResponseDTO> toModel(
            ReviewResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(
                        methodOn(ReviewController.class)
                                .findById(
                                        dto.getIdReviews()
                                )
                ).withSelfRel(),

                linkTo(
                        methodOn(ReviewController.class)
                                .findAll()
                ).withRel("reviews"),

                linkTo(
                        methodOn(ReviewController.class)
                                .findByBooking(
                                        dto.getBookingId()
                                )
                ).withRel("booking-reviews")
        );
    }
}