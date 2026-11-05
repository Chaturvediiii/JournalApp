package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.JournalEntryEntity;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import net.engineeringdigest.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    JournalEntryRepo journalEntryRepo;

    @Autowired
    UserService userService;

    @Transactional
    public void saveEntry(JournalEntryEntity journalEntryEntity, String username){
        try{
            UserEntity user = userService.findByUsername(username);
            journalEntryEntity.setDate(LocalDateTime.now());
            JournalEntryEntity entity = journalEntryRepo.save(journalEntryEntity);
            user.getJournalEntries().add(entity);
            userService.saveEntry(user);
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void saveEntry(JournalEntryEntity journalEntryEntity){
        journalEntryRepo.save(journalEntryEntity);
    }

    public List<JournalEntryEntity> getJournalEntries(String username){
        UserEntity user = userService.findByUsername(username);
        return user.getJournalEntries();
    }

    public Optional<JournalEntryEntity> findById(String id){
        return journalEntryRepo.findById(id);
    }

    public Boolean deleteJournalEntry(String id, String username){
        UserEntity user = userService.findByUsername(username);
        for (JournalEntryEntity entity: user.getJournalEntries()){
            if(entity.getId().equals(id)){
                user.getJournalEntries().remove(entity);
                userService.saveEntry(user);
                journalEntryRepo.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
