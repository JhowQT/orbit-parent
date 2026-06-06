@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository repository;

    private final DestinationRepository destinationRepository;

    private final BookingStatusRepository bookingStatusRepository;

    private final BookingMapper mapper;

    private final AuthClient authClient;

    private final AiClient aiClient;

    private final BookingProducer bookingProducer;

    public BookingResponseDTO create(
            BookingCreateDTO dto) {

        if (dto.getDepartureDate() == null
                || dto.getReturnDate() == null) {

            throw new IllegalArgumentException(
                    "Datas de ida e volta são obrigatórias."
            );
        }

        if (!dto.getDepartureDate()
                .isBefore(dto.getReturnDate())) {

            throw new IllegalArgumentException(
                    "A data de ida deve ser anterior à data de volta."
            );
        }

        if (dto.getNumPassengers() == null
                || dto.getNumPassengers() <= 0) {

            throw new IllegalArgumentException(
                    "A quantidade de passageiros deve ser maior que zero."
            );
        }

        UserResponseDTO user =
                authClient.findUserById(
                        dto.getUserId()
                );

        if (user == null) {

            throw new ResourceNotFoundException(
                    "Usuário não encontrado."
            );
        }

        if (dto.getAiRecommendationId() != null) {

            aiClient.findRecommendationById(
                    dto.getAiRecommendationId()
            );
        }

        Destination destination =
                destinationRepository
                        .findById(
                                dto.getDestinationId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destino não encontrado. ID: "
                                                + dto.getDestinationId()
                                )
                        );

        if (dto.getNumPassengers()
                > destination.getCapacity()) {

            throw new IllegalArgumentException(
                    "Quantidade de passageiros excede a capacidade do destino."
            );
        }

        BookingStatus bookingStatus =
                bookingStatusRepository
                        .findByName("PENDING")
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Status PENDING não encontrado."
                                )
                        );

        Booking booking =
                mapper.toEntity(dto);

        booking.setDestination(destination);

        booking.setBookingStatus(
                bookingStatus
        );

        booking.setCreatedAt(
                LocalDateTime.now()
        );

        booking.setTotalPrice(
                destination.getBasePrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        dto.getNumPassengers()
                                )
                        )
        );

        Booking saved =
                repository.save(
                        booking
                );

        bookingProducer.sendBookingCreated(
                BookingCreatedEvent.builder()
                        .bookingId(
                                saved.getIdBookings()
                        )
                        .userId(
                                saved.getUserId()
                        )
                        .destinationId(
                                destination.getIdDestinations()
                        )
                        .totalPrice(
                                saved.getTotalPrice()
                        )
                        .createdAt(
                                saved.getCreatedAt()
                        )
                        .build()
        );

        return mapper.toResponseDTO(
                saved
        );
    }

    @Transactional(readOnly = true)
    public BookingResponseDTO findById(
            Long id) {

        Booking booking =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reserva não encontrada. ID: "
                                                + id
                                )
                        );

        return mapper.toResponseDTO(
                booking
        );
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDTO> findByUser(
            Long userId) {

        return repository.findByUserId(
                        userId
                )
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public BookingResponseDTO update(
            Long id,
            BookingUpdateDTO dto) {

        Booking booking =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reserva não encontrada. ID: "
                                                + id
                                )
                        );

        if (!dto.getDepartureDate()
                .isBefore(dto.getReturnDate())) {

            throw new IllegalArgumentException(
                    "A data de ida deve ser anterior à data de volta."
            );
        }

        if (dto.getNumPassengers() <= 0) {

            throw new IllegalArgumentException(
                    "A quantidade de passageiros deve ser maior que zero."
            );
        }

        Destination destination =
                destinationRepository
                        .findById(
                                dto.getDestinationId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destino não encontrado. ID: "
                                                + dto.getDestinationId()
                                )
                        );

        if (dto.getNumPassengers()
                > destination.getCapacity()) {

            throw new IllegalArgumentException(
                    "Quantidade de passageiros excede a capacidade do destino."
            );
        }

        BookingStatus bookingStatus =
                bookingStatusRepository
                        .findById(
                                dto.getBookingStatusId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Status não encontrado. ID: "
                                                + dto.getBookingStatusId()
                                )
                        );

        booking.setDepartureDate(
                dto.getDepartureDate()
        );

        booking.setReturnDate(
                dto.getReturnDate()
        );

        booking.setNumPassengers(
                dto.getNumPassengers()
        );

        booking.setDestination(
                destination
        );

        booking.setBookingStatus(
                bookingStatus
        );

        booking.setTotalPrice(
                destination.getBasePrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        dto.getNumPassengers()
                                )
                        )
        );

        Booking updated =
                repository.save(
                        booking
                );

        return mapper.toResponseDTO(
                updated
        );
    }

    public BookingResponseDTO cancelBooking(
            Long id) {

        Booking booking =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reserva não encontrada. ID: "
                                                + id
                                )
                        );

        if ("CANCELLED".equalsIgnoreCase(
                booking.getBookingStatus()
                        .getName())) {

            throw new IllegalArgumentException(
                    "A reserva já está cancelada."
            );
        }

        BookingStatus cancelledStatus =
                bookingStatusRepository
                        .findByName(
                                "CANCELLED"
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Status CANCELLED não encontrado."
                                )
                        );

        booking.setBookingStatus(
                cancelledStatus
        );

        Booking updated =
                repository.save(
                        booking
                );

        return mapper.toResponseDTO(
                updated
        );
    }

    public void delete(
            Long id) {

        Booking booking =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reserva não encontrada. ID: "
                                                + id
                                )
                        );

        repository.delete(
                booking
        );
    }
}