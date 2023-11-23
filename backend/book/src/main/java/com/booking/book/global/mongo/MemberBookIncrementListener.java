package com.booking.book.global.mongo;

import com.booking.book.memberbook.domain.MemberBook;
import com.booking.book.memberbook.service.MemberBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberBookIncrementListener extends AbstractMongoEventListener<MemberBook> {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final MemberBookService memberBookService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<MemberBook> event) {
//        event.getSource().setAutoIncrementId(sequenceGeneratorService.generateSequence(MemberBook.SEQUENCE_NAME));
    }
}
