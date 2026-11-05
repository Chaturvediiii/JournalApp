package net.engineeringdigest.journalApp.controllers;

import net.engineeringdigest.journalApp.entity.JournalEntryEntity;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    JournalEntryService  journalEntryService;
    @Autowired
    UserService userService;


    @PostMapping()
    public ResponseEntity<JournalEntryEntity> createEntry(@RequestBody JournalEntryEntity journalEntryEntity){
        if(journalEntryEntity.getTitle() == null || journalEntryEntity.getContent() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            journalEntryService.saveEntry(journalEntryEntity,username);
            return new ResponseEntity<>(journalEntryEntity, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<JournalEntryEntity>> getAllJournalEntriesOfUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.findByUsername(username);
        List<JournalEntryEntity> entries = user.getJournalEntries();

        if(entries!=null){
            return new ResponseEntity<>(entries, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id")
    public ResponseEntity<JournalEntryEntity> getJournalEntryById(@RequestParam String id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.findByUsername(username);
        for(JournalEntryEntity je: user.getJournalEntries()){
            if(je.getId().equals(id)){
                Optional<JournalEntryEntity> entry = journalEntryService.findById(id);

                if(entry.isPresent()){
                    return new ResponseEntity<>(entry.get(), HttpStatus.OK);
                }
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteEntry(@RequestParam String id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.findByUsername(username);
        Boolean removed = journalEntryService.deleteJournalEntry(id,username);
        if(removed){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping()
    public ResponseEntity<JournalEntryEntity> updateEntry(@RequestParam String id, @RequestBody JournalEntryEntity journalEntryEntity){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.findByUsername(username);
        for(JournalEntryEntity je: user.getJournalEntries()){
            if(je.getId().equals(id)){
                JournalEntryEntity entity = journalEntryService.findById(id).orElse(null);
                if(entity != null){
                    entity.setTitle(journalEntryEntity.getTitle()!=null && !journalEntryEntity.getTitle().isEmpty() ? journalEntryEntity.getTitle() : entity.getTitle());
                    entity.setContent(journalEntryEntity.getContent()!=null && !journalEntryEntity.getContent().isEmpty() ? journalEntryEntity.getContent() : entity.getContent());
                    journalEntryService.saveEntry(entity);
                    return new ResponseEntity<>(entity, HttpStatus.CREATED);
                }
            }
        }
        return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
