package momotarian.controller;

import com.google.gson.Gson;
import de.htw.ai.momotarian.controller.SongListControllerDI;
import de.htw.ai.momotarian.model.SongList;
import de.htw.ai.momotarian.service.AuthService;
import de.htw.ai.momotarian.service.ISongListService;
import de.htw.ai.momotarian.service.SongListService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class SongListControllerDITest {

    int testIdExistent;
    int testIdExistent2;
    int testIdNichtExistent;

    String testLocation;

    String tokenValid;
    String tokenValid2;
    String tokenInvalid;
    String tokenEmpty;

    String testJSONBodyGood;
    String testJSONBodyNoTitle;
    String testJSONBodyBad;
    String returnStringJSON;
    String testJSONBodyWrongSong;
    String returnStringJSONWrongSong;

    SongList testSongList;
    SongList testSongListWrongSong;
    Set<SongList> testSongListSet;

    String authorizedUserId;
    String notValidUserId;

    ISongListService mockSongListService;
    AuthService mockAuthService;
    SongListControllerDI songListController;

    @BeforeEach
    public void setup() throws NotFoundException {

        mockSongListService = Mockito.mock(SongListService.class);
        mockAuthService = Mockito.mock(AuthService.class);
        songListController = new SongListControllerDI();
        songListController.setServices(mockSongListService, mockAuthService);


        testIdExistent = 1;
        testIdExistent2 = 2;
        testIdNichtExistent = 200;

        tokenValid = "validToken";
        tokenValid2 = "also valid, different User";
        tokenEmpty = "";

        authorizedUserId = "eschuler";
        notValidUserId = "keinUser";
        testLocation = "http://localhost:8080/songs/playist/";

        Gson gson = new Gson();

        testJSONBodyGood = "{ \"name\": \"Schuler Takeover\", \"songList\": " +
                "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
                "\"isPrivate\": true}";
        testJSONBodyWrongSong = "{ \"name\": \"Schuler Takeover\", \"songList\": " +
                "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"Peter Maffay\",\"label\": \"RCA\",\"released\": 2013 }]," +
                "\"private\": true,\"isPrivate\": true}";
        testJSONBodyNoTitle = "{ \"songList\": " +
                "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
                "\"private\": true,\"isPrivate\": true}";
        testJSONBodyBad = "Bei Gott kein JSON_Body";
        returnStringJSON = "{ \"id\": 1,\"userId\": \"eschuler\",\"name\": \"Schuler Takeover\", \"songList\": " +
                "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
                "\"private\": true,\"isPrivate\": true}";
        returnStringJSONWrongSong = "{ \"id\": 1,\"userId\": \"eschuler\",\"name\": \"Schuler Takeover\", \"songList\": " +
                "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"Peter Maffay\",\"label\": \"RCA\",\"released\": 2013 }]," +
                "\"private\": true,\"isPrivate\": true}";

        testSongList = gson.fromJson(returnStringJSON, SongList.class);
        testSongListWrongSong = gson.fromJson(returnStringJSONWrongSong, SongList.class);
        testSongListSet = new HashSet<>();
        testSongListSet.add(testSongList);

        when(mockSongListService.getSongList(testIdExistent)).thenReturn(testSongList);
        when(mockSongListService.getSongList(testIdNichtExistent)).thenThrow(NotFoundException.class);
        when(mockSongListService.postSongList(testSongListWrongSong, authorizedUserId)).thenThrow(NotFoundException.class);
        when(mockSongListService.getSongListSetByUser(authorizedUserId, true)).thenReturn(testSongListSet);
        doThrow(NotFoundException.class).when(mockSongListService).updateSongInSongList(testSongListWrongSong);

        when(mockAuthService.getUserIdByToken(tokenValid)).thenReturn(authorizedUserId);
        when(mockAuthService.isTokenValid(tokenValid)).thenReturn(true);
        when(mockAuthService.isTokenValid(tokenValid2)).thenReturn(true);
        when(mockAuthService.isTokenValid(tokenInvalid)).thenReturn(false);
        when(mockAuthService.isTokenValid(tokenEmpty)).thenReturn(false);
        when(mockAuthService.isUserValid(authorizedUserId)).thenReturn(true);
        when(mockAuthService.isUserValid(notValidUserId)).thenReturn(false);
        when(mockAuthService.doTokenAndIdMatch(tokenValid, authorizedUserId)).thenReturn(true);
        when(mockAuthService.doTokenAndIdMatch(tokenValid2, authorizedUserId)).thenReturn(false);

    }

    //getSongListByIdTest
//    @Test
//    public void getSongListByIdTest1Good() {
//        assertTrue(songListController.getSongListById(testIdExistent, tokenValid).getStatusCode().equals(HttpStatus.OK));
//        SongList test = (SongList) songListController.getSongListById(testIdExistent, tokenValid).getBody();
//        Assertions.assertTrue(test.getName().equals(testSongList.getName()));
//    }
    @Test public void getSongListTest2BadId() {
        assertTrue(songListController.getSongListById(testIdNichtExistent, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
    }
    @Test public void getSongListTest3InvalidToken() {
        assertTrue(songListController.getSongListById(testIdExistent, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    @Test public void getSongListByIdTest4EmptyToken() {
        assertTrue(songListController.getSongListById(testIdExistent, tokenEmpty).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    @Test public void getSongListByIdTest5TokenAndIdDontMatch() {
        assertTrue(songListController.getSongListById(testIdExistent, tokenValid2).getStatusCode().equals(HttpStatus.FORBIDDEN));
    }
    //getSongListByUser
    @Test public void getSongListByUserTest1Good() {
        assertTrue(songListController.getSongListByUser(authorizedUserId, tokenValid).getStatusCode().equals(HttpStatus.OK));
    }
    @Test public void getSongListByUserTest2UserNotFound() {
        assertTrue(songListController.getSongListByUser(notValidUserId, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
    }
    @Test public void getSongListByUserTest3FalseToken() {
        assertTrue(songListController.getSongListByUser(authorizedUserId,tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    //postSongListTest
//    @Test public void postSongListTest1Good() throws NotFoundException {
//        when(mockSongListService.postSongList(testSongList, testSongList.getOwnerId())).thenReturn(testIdExistent);
//        assertTrue(songListController.postSongList(testSongList, tokenValid).getStatusCode().equals(HttpStatus.CREATED));
//        assertTrue(songListController.postSongList(testSongList, tokenValid).getHeaders().getLocation().toString()
//                .equals(testLocation + testIdExistent));
//    }
    @Test public void postSongListTest2BadSongs() {
        assertTrue(songListController.postSongList(testSongListWrongSong, tokenValid).getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }
    @Test public void postSongListTest3InvalidToken() {
        assertTrue(songListController.postSongList(testSongList, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    //putSongList-Test
//    @Test public void updateSongListTest1Good() {
//        assertTrue(songListController.updateSongList(testIdExistent,testSongList,tokenValid).getStatusCode().equals(HttpStatus.NO_CONTENT));
//    }
//    @Test public void updateSongListTest2BadSongs() {
//        assertTrue(songListController.updateSongList(testIdExistent,testSongListWrongSong,tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
//    }
    @Test public void updateSongListTest3Forbidden() {
        assertTrue(songListController.updateSongList(testIdExistent, testSongList, tokenValid2).getStatusCode().equals(HttpStatus.FORBIDDEN));
    }
    @Test public void updateSongListTest4FalseToken() {
        assertTrue(songListController.updateSongList(testIdExistent, testSongList, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    @Test public void updateSongListTest5NotFound() {
        assertTrue(songListController.updateSongList(testIdNichtExistent, testSongList, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
    }
    //deleteSongList-Test
//    @Test public void deleteSongListTest1Good() {
//        assertTrue(songListController.deleteSongList(testIdExistent,tokenValid).getStatusCode().equals(HttpStatus.NO_CONTENT));
//    }
    @Test public void deleteSongListTest2IdNotFound() {
        assertTrue(songListController.deleteSongList(testIdNichtExistent, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
    }
    @Test public void deleteSongListTest3Forbidden() {
        assertTrue(songListController.deleteSongList(testIdExistent,tokenValid2).getStatusCode().equals(HttpStatus.FORBIDDEN));
    }
    @Test public void deleteSongListTest4FalseToken() {
        assertTrue(songListController.deleteSongList(testIdExistent,tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    //getOwner
//    @Test public void getOwnerTest1Good() throws NotFoundException {
//        assertTrue(songListController.getOwner(testIdExistent).equals(authorizedUserId));
//    }
    @Test public void getOwnerTest2NotFound(){
        try{
            songListController.getOwner(testIdNichtExistent);
        }catch(NotFoundException e){
            System.out.println("Not Found Exception geworfen! Test erfolgreich!");
        }
    }

    @Test public void isSongListPrivateTest1True() throws NotFoundException {
        assertTrue(songListController.isSongListPrivate(1));
    }
    @Test public void isSongListPrivateTest2False() throws NotFoundException {
        testSongList.setIsPrivate(false);
        assertTrue(!songListController.isSongListPrivate(1));
    }
    @Test public void isSongListPrivateTest2NotFound(){
        try{
            songListController.isSongListPrivate(testIdNichtExistent);
        }catch(NotFoundException e){
            System.out.println("Not Found Exception geworfen! Test erfolgreich!");
        }
    }

}
