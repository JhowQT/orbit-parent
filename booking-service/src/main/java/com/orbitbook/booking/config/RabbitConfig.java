package com.orbitbook.booking.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE =
            "orbitbook.exchange";

    public static final String BOOKING_ROUTING_KEY =
            "booking.created";

    public static final String PAYMENT_ROUTING_KEY =
            "payment.approved";

    @Bean
    public TopicExchange orbitbookExchange() {

        return new TopicExchange(
                EXCHANGE
        );
    }

    @Bean
    public Queue bookingQueue() {

        return new Queue(
                "booking.queue"
        );
    }

    @Bean
    public Queue paymentQueue() {

        return new Queue(
                "payment.queue"
        );
    }

    @Bean
    public Binding bookingBinding(
            Queue bookingQueue,
            TopicExchange orbitbookExchange) {

        return BindingBuilder
                .bind(bookingQueue)
                .to(orbitbookExchange)
                .with(BOOKING_ROUTING_KEY);
    }

    @Bean
    public Binding paymentBinding(
            Queue paymentQueue,
            TopicExchange orbitbookExchange) {

        return BindingBuilder
                .bind(paymentQueue)
                .to(orbitbookExchange)
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory) {

        RabbitTemplate template =
                new RabbitTemplate(connectionFactory);

        template.setMessageConverter(
                jsonMessageConverter()
        );

        return template;
    }
}