package com.orbitbook.booking.messaging;

import com.orbitbook.booking.messaging.dto.PaymentApprovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentConsumer {

    @RabbitListener(queues = "payment.queue")
    public void onPaymentApproved(
            PaymentApprovedEvent event) {

        log.info(
                "[RECIBO] Pagamento aprovado. " +
                "ID Pagamento: {}, Reserva: {}, " +
                "Valor: R$ {}, Aprovado em: {}",
                event.getPaymentId(),
                event.getBookingId(),
                event.getAmount(),
                event.getApprovedAt()
        );

        log.info(
                "[RECIBO] Comprovante de pagamento " +
                "gerado para a reserva {}.",
                event.getBookingId()
        );
    }
}
