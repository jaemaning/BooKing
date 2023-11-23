package com.booking.booking.hashtag.dto.response;

import com.booking.booking.hashtag.domain.Hashtag;

public record HashtagResponse(
        long hashtagId,
        String content
) {
    public HashtagResponse(Hashtag hashtag) {
        this(hashtag.getHashtagId(), hashtag.getContent());
    }
}
