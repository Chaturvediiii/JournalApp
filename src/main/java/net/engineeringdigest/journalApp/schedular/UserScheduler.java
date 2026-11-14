package net.engineeringdigest.journalApp.schedular;

import net.engineeringdigest.journalApp.entity.Journal;
import net.engineeringdigest.journalApp.entity.SentimentData;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.repository.UserRepoImp;
import net.engineeringdigest.journalApp.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    
    @Autowired
    EmailService emailService;
    
    @Autowired
    UserRepoImp  userRepo;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 0 9 * * SUN", zone = "Asia/Kolkata")
    public void fetchUsersAndSendSAMail(){
        System.out.println("SCHEDULER EXECUTED: " + LocalDateTime.now());

        List<User> users = userRepo.getUsers();
        for(User user:users){
            List<Journal> journalEntries = user.getJournalEntries();

            List<Sentiment> sentiments =  journalEntries.stream().filter(x->x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCounts = new HashMap<>();

            for(Sentiment sentiment:sentiments){
                if(sentiment!=null){
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }

            int maxCount = 0;
            Sentiment maxSentiment=null;
            for(Map.Entry<Sentiment,Integer> entry:sentimentCounts.entrySet()){
                if(entry.getValue()>maxCount){
                    maxCount=entry.getValue();
                    maxSentiment=entry.getKey();
                }
            }
            if(maxSentiment!=null){
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days "+maxSentiment).build();
                try{
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(),sentimentData);
                }
                catch(Exception e){
                    emailService.sendSimpleMail(sentimentData.getEmail(),"Sentiment for previous week",sentimentData.getSentiment());
                }
            }
        }
    }
}
