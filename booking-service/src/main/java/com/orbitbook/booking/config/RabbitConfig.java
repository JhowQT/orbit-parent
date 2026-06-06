package com.orbitbook.booking.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE =
            "orbitbook.exchange";

    public static final String BOOKING_QUEUE =
            "booking.queue";

    public static final String PAYMENT_QUEUE =
            "payment.queue";

    @Bean
    public TopicExchange exchange() {

        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue bookingQueue() {

        return new Queue(BOOKING_QUEUE);
    }

    @Bean
    public Queue paymentQueue() {

        return new Queue(PAYMENT_QUEUE);
    }

    @Bean
    public Binding bookingBinding(
            Queue bookingQueue,
            TopicExchange exchange) {

        return BindingBuilder
                .bind(bookingQueue)
                .to(exchange)
                .with("booking.created");
    }

    @Bean
    public Binding paymentBinding(
            Queue paymentQueue,
            TopicExchange exchange) {

        return BindingBuilder
                .bind(paymentQueue)
                .to(exchange)
                .with("payment.approved");
    }
}