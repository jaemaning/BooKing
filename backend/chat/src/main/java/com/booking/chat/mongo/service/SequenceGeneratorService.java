package com.booking.chat.mongo.service;

import com.booking.chat.mongo.util.DatabaseSequence;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
            Query.query(Criteria.where("_id").is(seqName)),
            new Update().inc("seq", 1),
            FindAndModifyOptions.options().returnNew(true).upsert(true),
            DatabaseSequence.class
        );
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
