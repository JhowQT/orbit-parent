package com.orbitbook.booking.messaging;

import com.orbitbook.booking.messaging.dto.PaymentApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentApproved(
            PaymentApprovedEvent event) {

        rabbitTemplate.convertAndSend(
                "orbitbook.exchange",
                "payment.approved",
                event
        );
    }
}