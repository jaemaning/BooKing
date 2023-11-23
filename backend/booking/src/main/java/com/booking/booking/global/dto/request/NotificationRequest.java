package com.booking.booking.global.dto.request;

import java.util.List;

public record NotificationRequest(
        List<Integer> memberList,
        String meetingTitle,
        NotificationType notificationType
) {
}
