package com.booking.chat.kafka.configuration;

import com.booking.chat.kafka.domain.KafkaMessage;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

// consumer 설정
@Configuration
@EnableKafka
public class ListenerConfiguration {

    @Value("${kafka.server.port}")
    private String KAFKA_PORT;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        return containerFactory;
    }

    @Bean
    public ConsumerFactory<String, KafkaMessage> consumerFactory() {
        JsonDeserializer<KafkaMessage> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");

        Map<String, Object> consumerConfiguration = ImmutableMap.<String, Object>builder()
                                                                .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PORT)
                                                                .put(ConsumerConfig.GROUP_ID_CONFIG, "chat")
                                                                .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                                                                .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                                                                .put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false) // 토픽 자동 생성
                                                                .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                                                                .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfiguration, new StringDeserializer(), deserializer);
    }

}
