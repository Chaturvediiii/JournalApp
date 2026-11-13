package net.engineeringdigest.journalApp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key,  Class<T> weatherResponseClass)  {
        try{
            Object o =  redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(),weatherResponseClass);
        }
        catch (Exception ex){
            return null;
        }
    }

    public void set(String key,  Object o, Long ttl)  {
        try{
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,json,ttl, TimeUnit.SECONDS);
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
