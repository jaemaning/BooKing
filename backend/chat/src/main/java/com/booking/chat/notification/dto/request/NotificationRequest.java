package com.booking.chat.notification.dto.request;

import com.booking.chat.notification.domain.NotificationType;
import java.util.List;

public record NotificationRequest(
    List<Long> memberList,
    String meetingTitle,
    NotificationType notificationType
) {

}
