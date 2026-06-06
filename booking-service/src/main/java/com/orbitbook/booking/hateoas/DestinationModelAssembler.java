package com.orbitbook.booking.hateoas;

import com.orbitbook.booking.controller.DestinationController;
import com.orbitbook.booking.dto.destination.DestinationResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DestinationModelAssembler
        implements RepresentationModelAssembler<
        DestinationResponseDTO,
        EntityModel<DestinationResponseDTO>> {

    @Override
    public EntityModel<DestinationResponseDTO> toModel(
            DestinationResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(
                        methodOn(DestinationController.class)
                                .findById(
                                        dto.getIdDestinations()
                                )
                ).withSelfRel(),

                linkTo(
                        methodOn(DestinationController.class)
                                .findAll()
                ).withRel("destinations"),

                linkTo(
                        methodOn(DestinationController.class)
                                .searchByName(
                                        dto.getName()
                                )
                ).withRel("search")
        );
    }
}