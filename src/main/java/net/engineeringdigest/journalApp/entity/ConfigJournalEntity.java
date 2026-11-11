package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("config")
public class ConfigJournalEntity {
    @Id
    private String id;
    private String key;
    private String value;
}
