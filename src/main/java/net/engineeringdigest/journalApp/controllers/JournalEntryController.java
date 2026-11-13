package net.engineeringdigest.journalApp.controllers;

import net.engineeringdigest.journalApp.entity.Journal;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Journal> createEntry(@RequestBody Journal journal){
        if(journal.getTitle() == null || journal.getContent() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            journalEntryService.saveEntry(journal,username);
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Journal>> getAllJournalEntriesOfUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        List<Journal> entries = user.getJournalEntries();

        if(entries!=null){
            return new ResponseEntity<>(entries, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id")
    public ResponseEntity<Journal> getJournalEntryById(@RequestParam String id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        for(Journal je: user.getJournalEntries()){
            if(je.getId().equals(id)){
                Optional<Journal> entry = journalEntryService.findById(id);

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
        User user = userService.findByUsername(username);
        Boolean removed = journalEntryService.deleteJournalEntry(id,username);
        if(removed){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping()
    public ResponseEntity<Journal> updateEntry(@RequestParam String id, @RequestBody Journal journal){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        for(Journal je: user.getJournalEntries()){
            if(je.getId().equals(id)){
                Journal entity = journalEntryService.findById(id).orElse(null);
                if(entity != null){
                    entity.setTitle(journal.getTitle()!=null && !journal.getTitle().isEmpty() ? journal.getTitle() : entity.getTitle());
                    entity.setContent(journal.getContent()!=null && !journal.getContent().isEmpty() ? journal.getContent() : entity.getContent());
                    journalEntryService.saveEntry(entity);
                    return new ResponseEntity<>(entity, HttpStatus.CREATED);
                }
            }
        }
        return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
