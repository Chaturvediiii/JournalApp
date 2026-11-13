package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import net.engineeringdigest.journalApp.enums.Sentiment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class Journal {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;
}
