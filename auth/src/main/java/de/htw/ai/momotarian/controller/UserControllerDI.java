package de.htw.ai.momotarian.controller;

import de.htw.ai.momotarian.model.User;
import de.htw.ai.momotarian.repo.UserRepository;
import de.htw.ai.momotarian.service.AuthService;
import de.htw.ai.momotarian.service.IAuthService;
import javassist.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value="/songsWS-momotarian/rest/auth")
public class UserControllerDI {

    private final UserRepository userRepository;

    private static final String LOCAL_SERVER_PORT = "local.server.port";
    @Autowired
    IAuthService authService;

    public UserControllerDI (UserRepository uRepository) {
        this.userRepository = uRepository;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    // "authorize nicht per GET!!
    public ResponseEntity<String> authorize(@RequestBody User newUser)
            throws Exception {
        String newUserId = newUser.getUserId();
        String newUserPassword = newUser.getPassword();
        if (newUser == null || newUserId == null ||
                newUserPassword == null) {
            return new ResponseEntity<String>("Declined: No such user!",
                    HttpStatus.UNAUTHORIZED);
        }

        List<User> repoUser = userRepository.findUserByUserId(newUserId);
        if(repoUser.isEmpty()){
            return new ResponseEntity<String>("Declined: No such user!", HttpStatus.UNAUTHORIZED);
        }
        if (repoUser.get(0).getUserId().equals(newUser.getUserId()) && repoUser.get(0).getPassword().equals(newUser.getPassword())) {
            String token = generateToken(repoUser.get(0));
            return new ResponseEntity<String> (token, HttpStatus.OK);
        }
        return new ResponseEntity<String> ("Declined!!", HttpStatus.UNAUTHORIZED);
    }

    private String generateToken(User user) {
        String generatedString = RandomStringUtils.randomAlphabetic(20);
      //  User updateduser = userRepository.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("user", "id", user.getUserId()));
       // User newuser = new User.builder();
//        updateduser.setToken(generatedString);
//        updateduser.setUserId(user.getUserId());
//        updateduser.setFirstname(user.getFirstname());
//        updateduser.setLastname(user.getLastname());
//        updateduser.setPassword(user.getPassword());

        //userRepository.findUserByUserId(userid).get(0).setToken(generatedString);
//        user.setToken(generatedString);
//        userRepository.save(user);

        userRepository.updateUser(user.getUserId(), generatedString);
        return generatedString;
    }

//    //GET http://localhost:8080/songsWS-momotarian/rest/songs
//    //for Testing purpose : Eureka Registry
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getUser() throws IOException {
//        User userEureka = new User();
//        userEureka.setFirstname("San");
//        userEureka.setLastname("Shr");
//        userEureka.setPassword("something");
//        userEureka.setToken("2");
//        userEureka.setUserId("4");
//        return new ResponseEntity<>(userEureka, HttpStatus.OK);
//
//    }

    @GetMapping(value="/valid/{token}")
    public boolean isTokenValid(@PathVariable(value="token") String token){
        try {
            authService.getUserByToken(token);
//            System.out.println("token valid");
            return true;
        } catch (NotFoundException e) {
            e.printStackTrace();
//            System.out.println("token not valid");
            return false;
        }
    }

    @GetMapping(value="/match")
    public boolean doTokenAndIdMatch(@RequestParam(value="userId") String userId,
                                     @RequestParam(value="token") String token){
        try {
            authService.getUserByUserId(userId);
            if(authService.getUserByToken(token).getUserId().equals(userId)){
                return true;
            }else{
                return false;
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping(value="/validUser/{userId}")
    public boolean isUserValid(@PathVariable(value="userId") String userId){
        try {
            authService.getUserByUserId(userId);
            return true;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping(value="/getId/{token}")
    public String getUserIdByToken(@PathVariable(value="token") String token){
        try{
            return authService.getUserByToken(token).getUserId();
        }catch (NotFoundException e){
            return "User not found!";
        }
    }

    public void setAuthService (AuthService mockAuthService){
        this.authService = mockAuthService;
    }
}
