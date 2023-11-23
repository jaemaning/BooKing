package com.booking.chat.message.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document(collection = "chats")
public class Message {

    @Id
    private String _id;

    private Long chatroomId;

    private Long messageId;

    private Long memberId;

    private String content;

    private Integer readCount;

    // 메세지가 발행될 때, 그 방에 있던 사람 목록
    private List<Long> memberList;
    // 읽은 사람 목록
    private Set<Long> readMemberList;

    @CreatedDate
    private LocalDateTime timestamp;


    public void setAutoIncrementId() {}

    public void decreaseReadCount() {
        this.readCount--;

        if(this.readCount < 0) this.readCount = 0;
    }

}
