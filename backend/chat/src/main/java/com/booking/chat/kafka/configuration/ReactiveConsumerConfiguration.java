package com.booking.chat.kafka.configuration;

import com.booking.chat.kafka.domain.KafkaMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

@Configuration
public class ReactiveConsumerConfiguration {

    @Value("${kafka.server.port}")
    private String KAFKA_PORT;

    @Bean
    public ReactiveKafkaConsumerTemplate<String, KafkaMessage> reactiveKafkaConsumerTemplate() {
        JsonDeserializer<KafkaMessage> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PORT);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "chat");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        ReceiverOptions<String, KafkaMessage> receiverOptions = ReceiverOptions.create(props);
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }

}
