package com.orbitbook.booking.service;

import com.orbitbook.booking.dto.review.ReviewCreateDTO;
import com.orbitbook.booking.dto.review.ReviewResponseDTO;
import com.orbitbook.booking.dto.review.ReviewUpdateDTO;
import com.orbitbook.booking.dto.review.ReviewWithUserDTO;
import com.orbitbook.booking.entity.Booking;
import com.orbitbook.booking.entity.Review;
import com.orbitbook.booking.exception.ResourceNotFoundException;
import com.orbitbook.booking.feign.AuthClient;
import com.orbitbook.booking.mapper.ReviewMapper;
import com.orbitbook.booking.repository.BookingRepository;
import com.orbitbook.booking.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository repository;

    private final BookingRepository bookingRepository;

    private final ReviewMapper mapper;

    private final AuthClient authClient;

    public ReviewResponseDTO createReview(
            ReviewCreateDTO dto) {

        if (dto.getRating() == null
                || dto.getRating() < 1
                || dto.getRating() > 5) {

            throw new IllegalArgumentException(
                    "A nota deve estar entre 1 e 5."
            );
        }

        Booking booking =
                bookingRepository.findById(
                                dto.getBookingId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reserva não encontrada. ID: "
                                                + dto.getBookingId()
                                )
                        );

        Review review =
                mapper.toEntity(dto);

        review.setBooking(
                booking
        );

        review.setCreatedAt(
                LocalDateTime.now()
        );

        Review saved =
                repository.save(review);

        return mapper.toResponseDTO(
                saved
        );
    }

    @Transactional(readOnly = true)
    public ReviewResponseDTO findById(
            Long id) {

        Review review =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Avaliação não encontrada. ID: "
                                                + id
                                )
                        );

        return mapper.toResponseDTO(
                review
        );
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findByBooking(
            Long bookingId) {

        return repository
                .findByBooking_IdBookings(
                        bookingId
                )
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewWithUserDTO> findByDestination(
            Long destinationId) {

        return repository.findByDestinationId(destinationId)
                .stream()
                .map(review -> {
                    Long userId = review.getBooking().getUserId();
                    String userName;
                    try {
                        userName = authClient.findUserById(userId).getName();
                    } catch (Exception e) {
                        userName = "Desconhecido";
                    }
                    return ReviewWithUserDTO.builder()
                            .idReviews(review.getIdReviews())
                            .rating(review.getRating())
                            .comment(review.getComment())
                            .createdAt(review.getCreatedAt())
                            .bookingId(review.getBooking().getIdBookings())
                            .userId(userId)
                            .userName(userName)
                            .destinationId(destinationId)
                            .build();
                })
                .toList();
    }

    public ReviewResponseDTO update(
            Long id,
            ReviewUpdateDTO dto) {

        Review review =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Avaliação não encontrada. ID: "
                                                + id
                                )
                        );

        if (dto.getRating() == null
                || dto.getRating() < 1
                || dto.getRating() > 5) {

            throw new IllegalArgumentException(
                    "A nota deve estar entre 1 e 5."
            );
        }

        review.setRating(
                dto.getRating()
        );

        review.setComment(
                dto.getComment()
        );

        Review updated =
                repository.save(review);

        return mapper.toResponseDTO(
                updated
        );
    }

    public void delete(
            Long id) {

        Review review =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Avaliação não encontrada. ID: "
                                                + id
                                )
                        );

        repository.delete(
                review
        );
    }
}
