package momotarian;

import com.google.gson.Gson;
import de.htw.ai.momotarian.controller.UserControllerDI;
import de.htw.ai.momotarian.model.User;
import de.htw.ai.momotarian.repo.UserRepository;
import de.htw.ai.momotarian.service.AuthService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class UserControllerDITest {

    UserControllerDI userController;

    UserRepository userRepository;

    AuthService mockAuthService;

    User user1;
    User user2;

    String userId1;
    String userId2;

    String password1;
    String password2;

    String user1json;
    String user2json;

    Gson gson;

    String tokenValid;
    String tokenInvalid;

    @BeforeEach
    public void setUp() throws NotFoundException {
        userController = new UserControllerDI(userRepository);
        mockAuthService = Mockito.mock(AuthService.class);
        userController.setAuthService(mockAuthService);

        user1 = new User();
        user2 = new User();

        userId1 = "userId1";
        userId2 = "userId2";

        password1 = "password1";
        password2 = "password2";

        user1.setUserId(userId1);
        user2.setUserId(userId2);

        user1.setPassword(password1);
        user2.setPassword(password2);

        gson = new Gson();

        user1json = gson.toJson(user1,User.class);
        user2json = gson.toJson(user2,User.class);

        tokenValid = "tokenValid";
        tokenInvalid = "tokenInvalid";

        when(mockAuthService.getUserByUserId(userId1)).thenReturn(user1);
        when(mockAuthService.getUserByUserId(userId2)).thenThrow(NotFoundException.class);
        when(mockAuthService.getUserByToken(tokenValid)).thenReturn(user1);
        when(mockAuthService.getUserByToken(tokenInvalid)).thenThrow(NotFoundException.class);
    }
    //authorize
//    @Test
//    public void authorizeTest1Good() throws Exception {
//        assertTrue(userController.authorize(user1).getStatusCode().equals(HttpStatus.OK));
//    }
//    @Test public void authorizeTest2WrongPW() throws Exception {
//        user1.setPassword(password2);
//        assertTrue(userController.authorize(user1).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
//    }
//    @Test public void authorizeTest3NotFound() throws Exception {
//        assertTrue(userController.authorize(user2).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
//        assertTrue(userController.authorize(user2).getBody().equals("Declined: No such user!"));
//    }

    //isTokenValid
    @Test public void isTokenValidTest1Good(){
        assertTrue(userController.isTokenValid(tokenValid));
    }
    @Test public void isTokenValidTest2NotValid(){
        assertTrue(!userController.isTokenValid(tokenInvalid));
    }
    //doTokenAndIdMatch
    @Test public void doTokenAndIdMatchTest1Good(){
        assertTrue(userController.doTokenAndIdMatch(userId1,tokenValid));
    }
    @Test public void doTokenAndIdMatchTest2false(){
        assertTrue(!userController.doTokenAndIdMatch("user3",tokenValid));
    }
    @Test public void doTokenAndIdMatchTest3NotFound(){
        assertTrue(!userController.doTokenAndIdMatch(userId2,tokenValid));
    }
    //isUserValid
    @Test public void isUserValidTest1Good(){
        assertTrue(userController.isUserValid(userId1));
    }
    @Test public void isUserValidTest2NotFound(){
        assertTrue(!userController.isUserValid(userId2));
    }
    //getUserIdByToken
    @Test public void getUserIdByTokenTest1Good(){
        assertTrue(userController.getUserIdByToken(tokenValid).equals(userId1));
    }
    @Test public void getUserIdByTokenTest2NotFound(){
        assertTrue(userController.getUserIdByToken(tokenInvalid).equals("User not found!"));
    }
}
