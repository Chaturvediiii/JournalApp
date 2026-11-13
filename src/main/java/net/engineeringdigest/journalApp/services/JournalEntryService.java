package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.Journal;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    JournalEntryRepo journalEntryRepo;

    @Autowired
    UserService userService;

    @Transactional
    public void saveEntry(Journal journal, String username){
        try{
            User user = userService.findByUsername(username);
            journal.setDate(LocalDateTime.now());
            Journal entity = journalEntryRepo.save(journal);
            user.getJournalEntries().add(entity);
            userService.saveEntry(user);
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void saveEntry(Journal journal){
        journalEntryRepo.save(journal);
    }

    public List<Journal> getJournalEntries(String username){
        User user = userService.findByUsername(username);
        return user.getJournalEntries();
    }

    public Optional<Journal> findById(String id){
        return journalEntryRepo.findById(id);
    }

    public Boolean deleteJournalEntry(String id, String username){
        User user = userService.findByUsername(username);
        for (Journal entity: user.getJournalEntries()){
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
