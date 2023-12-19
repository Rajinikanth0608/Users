package com.example.UserManagement.User.Service;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.UserManagement.User.Entity.DatabaseSequence;
    @Service

        public class SequenceGeneratorService {
    
        @Autowired    
        private static MongoOperations mongoOperations;
	
	
        public SequenceGeneratorService(MongoOperations mongoOperations) {
        SequenceGeneratorService.mongoOperations = mongoOperations;
        }
 
	    public static int getSeqNumber(String sequenceName) {
		
		Query query = new Query(Criteria.where("id").is(sequenceName));
		Update update = new Update().inc("seqNo", 1);
 
		
		FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        options.upsert(true);
 
        DatabaseSequence counter = mongoOperations.findAndModify(query,update, options, DatabaseSequence.class);
		return !Objects.isNull(counter) ? counter.getSeqNo() : 1;
		
	} 
}

