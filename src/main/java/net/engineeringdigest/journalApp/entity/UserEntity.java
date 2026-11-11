package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("users")
public class UserEntity {
    @Id
    private String id;
    @Indexed(unique=true)
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String email;
    private String sentimentAnalysis;

    @DBRef
    private List<JournalEntryEntity> journalEntries = new ArrayList<>();

    private List<String> roles;
}
