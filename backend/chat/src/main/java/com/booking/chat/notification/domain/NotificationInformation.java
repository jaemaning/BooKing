package com.booking.chat.notification.domain;


import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document(collection = "notificationInformation")
public class NotificationInformation {

    @Id
    private UUID _id;

    private Long memberId;

    private String deviceToken;

    public void update(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
