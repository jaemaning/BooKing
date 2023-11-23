package com.booking.chat.notification.dto.response;

import java.util.Map;

public record NotificationResponse(
    String title,
    String body,
    String memberName,
    Long memberId,
    Map<String, String> data
) {

}
