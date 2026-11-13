package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.Journal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepo extends MongoRepository<Journal,String> {
}
