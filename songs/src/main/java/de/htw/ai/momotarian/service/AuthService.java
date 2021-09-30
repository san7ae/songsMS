package de.htw.ai.momotarian.service;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private RestTemplate restTemplate;

    public AuthService() { restTemplate = new RestTemplate();}

    public Boolean isTokenValid(String token) {
        return true;
//        return restTemplate.getForObject("http://localhost:8080/songsWS-momotarian/rest/auth/valid/"+token, Boolean.class);
    }

    public Boolean doTokenAndIdMatch(String token, String userId){
        return true;
//        return restTemplate.getForObject("http://localhost:8080/songsWS-momotarian/rest/auth/match?token="+token+"&userId="+userId, Boolean.class);
    }


    public String getUserIdByToken(String token) throws NotFoundException {
        String userID = restTemplate.getForObject("http://localhost:8080/songsWS-momotarian/rest/auth/getId/"+token, String.class);
        if(userID.equals("User not found!")){
            throw new NotFoundException("No such User.");
        }
        return userID;
    }

    public boolean isUserValid(String userId) {
        return true;
//        return restTemplate.getForObject("http://localhost:8080/songsWS-momotarian/rest/auth/validUser/"+userId, Boolean.class);
    }
}
