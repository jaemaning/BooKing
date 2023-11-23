package com.booking.chat.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    ENROLL("%s 모임에 가입 신청이 들어왔습니다."), CONFIRM("%s 모임이 확정되었습니다.");

    private final String message;

    public static String makeNotificationBody(NotificationType notificationType, String meetingTitle) {
        return notificationType.message.formatted(meetingTitle);
    }
}
