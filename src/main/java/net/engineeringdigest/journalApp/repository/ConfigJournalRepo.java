package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.ConfigJournalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalRepo extends MongoRepository<ConfigJournalEntity,String> {
}
