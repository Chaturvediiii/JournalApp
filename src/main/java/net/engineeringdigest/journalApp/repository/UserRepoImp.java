package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepoImp {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<User> getUsers() {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));
        query.addCriteria((Criteria.where("sentimentAnalysis").is(true)));
        List<User> users = mongoTemplate.find(query, User.class);
        for(User user:users){
            System.out.println(user.getEmail());
        }
        return users;
    }
}
