package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Service
public class SentimentConsumerService {
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics="weekly-sentiments", groupId = "weekly_sentiment_group")
    public void consume(SentimentData sentimentData) {
      sendEmail(sentimentData);
    }

    public void sendEmail(SentimentData sentimentData) {

        emailService.sendSimpleMail(sentimentData.getEmail(),"Sentiment for previous week",sentimentData.getSentiment());
    }
}
