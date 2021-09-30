package momotarian.controller;

import com.google.gson.Gson;
import de.htw.ai.momotarian.controller.SongControllerDI;
import de.htw.ai.momotarian.model.Song;
import de.htw.ai.momotarian.service.AuthService;
import de.htw.ai.momotarian.service.ISongService;
import de.htw.ai.momotarian.service.SongService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class SongControllerDITest {

    //class-objects
    ISongService mockSongService;
    AuthService mockAuthRestWrapper;
    SongControllerDI songController;
    //ids
    int testIDExistent;
    int testIDNichtExistent;
    //location
    String testLocation;
    //token
    String tokenValid;
    String tokenInvalid;
    String tokenEmpty;
    //JSON-bodies
    String testJSONBodyGood;
    String testJSONBodyNoTitle;
    String testJSONBodyBad;
    String returnStringJSON;
    String getReturnStringXML;
    String testSongListJSON;
    //test-object
    Song testSong;
    List<Song> testSongList;

    @BeforeEach
    public void setUpForTests() throws Exception {
        mockSongService = Mockito.mock(SongService.class);
        mockAuthRestWrapper = Mockito.mock(AuthService.class);
        songController = new SongControllerDI();
        songController.setServices(mockSongService, mockAuthRestWrapper);

        testIDExistent = 1;
        testIDNichtExistent = 1000;

        testLocation = "http://localhost:8080/songs/";

        Gson gson = new Gson();

        tokenValid = "valid";
        tokenInvalid = "invalid";
        tokenEmpty = null;

        testJSONBodyGood = "{\"title\":\"Wrecking Ball\",\"artist\":\"MILEY"
                + "CYRUS\",\"label\":\"RCA\",\"released\":2013}";
        testJSONBodyNoTitle = "{\"artist\":\"MILEY"
                + "CYRUS\",\"label\":\"RCA\",\"released\":2013}";
        testJSONBodyBad = "Bei Gott kein JSON-Body";
        returnStringJSON = "{\"id\":0,\"title\":\"Wrecking Ball\",\"artist\":\"MILEY"
                + "CYRUS\",\"label\":\"RCA\",\"released\":2013}";
        getReturnStringXML = "<Songs>"+"<id>0</id>" + "<title>Wrecking Ball</title>" + "<artist>MILEY" +
                "CYRUS</artist>" + "<label>RCA</label>" + "<released>2013</released>"+"</Songs>";

        testSong = gson.fromJson(testJSONBodyGood, Song.class);
        testSongList = new ArrayList<>();
        testSongList.add(testSong);
        testSongList.add(testSong);
        testSongListJSON = gson.toJson(testSongList);

        when(mockAuthRestWrapper.isTokenValid(tokenValid)).thenReturn(true);
        when(mockAuthRestWrapper.isTokenValid(tokenInvalid)).thenReturn(false);
        when(mockAuthRestWrapper.isTokenValid(tokenEmpty)).thenReturn(false);
        when(mockSongService.getSong(testIDExistent)).thenReturn(testSong);
        when(mockSongService.getSong(testIDNichtExistent)).thenThrow(NotFoundException.class);
    }

    //getSongTests
    @Test
    public void getSongTest1Good(){
        assertTrue(songController.getSong(testIDExistent, tokenValid).getStatusCode().equals(HttpStatus.OK));
        Assertions.assertTrue(songController.getSong(testIDExistent, tokenValid).getBody().getTitle().equals(testSong.getTitle()));
    }
    @Test public void getSongTest2BadId(){
        assertTrue(songController.getSong(testIDNichtExistent, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
    }
    @Test public void getSongTest3InvalidToken(){
        assertTrue(songController.getSong(testIDExistent, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    @Test public void getSongTest4TokenIsNull(){
        assertTrue(songController.getSong(testIDExistent, tokenEmpty).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    //getAllSongsTests
    @Test public void allSongsTest1Good() throws NotFoundException {
        when(mockSongService.findAll()).thenReturn((ArrayList<Song>) testSongList);
        assertTrue(songController.allSongs(tokenValid).getStatusCode().equals(HttpStatus.OK));
    }
    @Test public void allSongsTest2NoSongs() throws NotFoundException {
        when(mockSongService.findAll()).thenThrow(NotFoundException.class);
        assertTrue(songController.allSongs(tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
    }
    @Test public void allSongsTest3InvalidToken(){
        assertTrue(songController.allSongs(tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    @Test public void allSongsTest4EmptyToken(){
        assertTrue(songController.allSongs(tokenEmpty).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    //postSongTests
    @Test public void postSongTest1Good() throws Exception {
        when(mockSongService.postSong(Mockito.any())).thenReturn(testIDExistent);
        assertTrue(songController.postSong(testJSONBodyGood, tokenValid).getStatusCode().equals(HttpStatus.CREATED));
        assertTrue(songController.postSong(testJSONBodyGood, tokenValid).getHeaders()
                .getLocation().toString().equals(testLocation+testIDExistent));
    }
    @Test public void postSongTest2NoTitle() throws Exception {
        when(mockSongService.postSong(Mockito.any())).thenThrow(Exception.class);
        assertTrue(songController.postSong(testJSONBodyNoTitle, tokenValid).getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }
    @Test public void postSongTest3BadJson(){
        assertTrue(songController.postSong(testJSONBodyBad, tokenValid).getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }
    @Test public void postSongTest4InvalidToken(){
        assertTrue(songController.postSong(testJSONBodyGood, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    @Test public void postSongTest5EmptyToken(){
        assertTrue(songController.postSong(testJSONBodyGood, tokenEmpty).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }
    //putsong
    @Test public void putSongTest1Good(){
        assertTrue(songController.putSong(returnStringJSON, testIDExistent, tokenValid).getStatusCode()
                .equals(HttpStatus.NO_CONTENT));
    }
    @Test public void putSongTest2BadJson(){
        assertTrue(songController.putSong(testJSONBodyBad, testIDExistent, tokenValid).getStatusCode()
                .equals(HttpStatus.BAD_REQUEST));
    }
    @Test public void putSongTest3NotMatchingId() throws NotFoundException {
        doThrow(InvalidParameterException.class).when(mockSongService).putSong(Mockito.anyInt(),Mockito.any());
        assertTrue(songController.putSong(returnStringJSON, 2, tokenValid).getStatusCode()
                .equals(HttpStatus.BAD_REQUEST));
    }
}
