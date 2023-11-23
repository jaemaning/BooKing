package com.booking.chat.kafka.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KafkaMessage implements Serializable {

    private String message;
    private Long senderId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;
    private String senderName;

    public Map<String, String> extractData(Long chatroomId) {
        Map<String, String> data = new HashMap<>();

        data.put("message", this.message);
        data.put("senderId", String.valueOf(this.senderId));
        data.put("sendTime", String.valueOf(this.sendTime));
        data.put("chatroomId", String.valueOf(chatroomId));
        data.put("senderName", senderName);

        return data;

    }

    public static KafkaMessage init() {
        return KafkaMessage.builder()
            .message("새로운 회원이 입장하셨습니다.")
            .senderId(null)
            .sendTime(LocalDateTime.now())
            .senderName("관리자")
            .build();
    }
}
