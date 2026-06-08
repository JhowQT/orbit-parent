package com.orbitbook.booking.messaging;

import com.orbitbook.booking.messaging.dto.BookingCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingConsumer {

    @RabbitListener(queues = "booking.queue")
    public void onBookingCreated(
            BookingCreatedEvent event) {

        log.info(
                "[NOTIFICAÇÃO] Nova reserva criada. " +
                "ID: {}, Usuário: {}, Destino: {}, " +
                "Total: R$ {}, Data: {}",
                event.getBookingId(),
                event.getUserId(),
                event.getDestinationId(),
                event.getTotalPrice(),
                event.getCreatedAt()
        );

        log.info(
                "[NOTIFICAÇÃO] E-mail de confirmação " +
                "enviado para o usuário {}.",
                event.getUserId()
        );
    }
}
