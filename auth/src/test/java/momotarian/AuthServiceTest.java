package momotarian;

import de.htw.ai.momotarian.model.User;
import de.htw.ai.momotarian.repo.UserRepository;
import de.htw.ai.momotarian.service.AuthService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    User userGood;
    User userBad;

    String userIdGood;
    String userIdBad;

    String tokenGood;
    String tokenBad;

    ArrayList<User> userList;

    AuthService authService;

    UserRepository mockUserRepository;

    @BeforeEach
    public void setUp(){
        authService = new AuthService();
        mockUserRepository = Mockito.mock(UserRepository.class);
        authService.setRepository(mockUserRepository);

        userIdGood = "goodUser";
        userIdBad = "badUser";

        userGood = new User();
        userGood.setUserId(userIdGood);

        userBad = new User();
        userBad.setUserId(userIdBad);

        tokenGood = "goodToken";
        tokenBad = "badToken";

        userGood.setToken(tokenGood);

        userList = new ArrayList<>();
        userList.add(userGood);

        when(mockUserRepository.findById(userIdGood)).thenReturn(Optional.of(userGood));
        when(mockUserRepository.findById(userIdBad)).thenThrow(EntityNotFoundException.class);
        when(mockUserRepository.findAll()).thenReturn(userList);
    }

    //getUserByUserId
    @Test
    public void getUserByUserIdGoodTest1() throws NotFoundException {
        assertTrue(authService.getUserByUserId(userIdGood).getUserId().equals(userIdGood));
    }
    @Test public void getUserByUserIdBadTest1(){
        try{
            authService.getUserByUserId(userIdBad);
        }catch (Exception e){
            assertTrue(e.getMessage().equals("Not User with this ID!"));
        }
    }
    //setToken
    @Test public void setTokenGoodTest1() throws NotFoundException {
        authService.setToken(userGood,tokenGood);
        Mockito.verify(mockUserRepository).findById(userIdGood);
        Mockito.verify(mockUserRepository).save(any());
    }
    @Test public void setTokenBadtest1(){
        try{
            authService.setToken(userBad, tokenGood);
        }catch (Exception e){
            assertTrue(e.getMessage().equals("Kein solcher User!"));
        }
    }
    //isTokenValid
    @Test public void isTokenValidGoodTest1(){
        assertTrue(authService.isTokenValid(tokenGood));
    }
    @Test public void isTokenValidBadTest1(){
        assertTrue(!authService.isTokenValid(tokenBad));
    }
    //getUserByToken
    @Test public void getUserByTokenGoodTest1() throws NotFoundException {
        assertTrue(authService.getUserByToken(tokenGood).getUserId().equals(userIdGood));
    }
    @Test public void getUserByTokenBadTest1(){
        try{
            authService.getUserByToken(tokenBad);
        }catch(Exception e){
            e.getMessage().equals("No User with this token");
        }
    }
}
