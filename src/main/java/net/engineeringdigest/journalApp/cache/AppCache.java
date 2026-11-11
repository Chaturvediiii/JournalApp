package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.entity.ConfigJournalEntity;
import net.engineeringdigest.journalApp.repository.ConfigJournalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {
    public Map<String, String> appCache = new HashMap<>();

    @Autowired
    ConfigJournalRepo configJournalRepo;

    @PostConstruct
    public void init(){
        List<ConfigJournalEntity> configJournalEntities = configJournalRepo.findAll();
        for(ConfigJournalEntity configJournalEntity : configJournalEntities){
            appCache.put(configJournalEntity.getKey(),configJournalEntity.getValue());
        }
    }
}
