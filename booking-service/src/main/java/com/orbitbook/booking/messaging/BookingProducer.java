package com.orbitbook.booking.messaging;

import com.orbitbook.booking.messaging.dto.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendBookingCreated(
            BookingCreatedEvent event) {

        rabbitTemplate.convertAndSend(
                "orbitbook.exchange",
                "booking.created",
                event
        );
    }
}