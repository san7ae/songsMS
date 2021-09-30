package de.htw.ai.momotarian.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import de.htw.ai.momotarian.exception.ResourceNotFoundException;
import de.htw.ai.momotarian.model.Song;
import de.htw.ai.momotarian.repo.SongRepository;
import de.htw.ai.momotarian.service.AuthService;
import de.htw.ai.momotarian.service.ISongService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

@RestController
@RequestMapping(value="/songsWS-momotarian/rest/songs")
public class SongControllerDI {

    @Autowired
    private AuthService authService;

    @Autowired
    private ISongService songService;

//    private final SongRepository songRepository;
//    private final UserRepository userRepository;
//
//    public SongControllerDI (SongRepository sRepository) {
//        this.songRepository = sRepository;
////        this.userRepository = uRepository;
//    }
//
//    @GetMapping(produces = "text/plain")
//    // "authorize nicht per GET!!
//    public ResponseEntity<String> authorize(
//                        @RequestParam("userId") String userId,
//                        @RequestParam("password") String password)
//                                            throws IOException {
//        User user = userDAO.getUserByUserId(userId);
//
//        if (user == null || user.getUserId() == null ||
//            user.getPassword() == null) {
//            return new ResponseEntity<String>("Declined: No such user!",
//                    HttpStatus.UNAUTHORIZED);
//        }
//
//        if (user.getUserId().equals(userId) && user.getPassword().equals(password)) {
//            return new ResponseEntity<String> ("Welcome!!", HttpStatus.OK);
//        }
//        return new ResponseEntity<String> ("Declined!!", HttpStatus.UNAUTHORIZED);
//    }
//
//    public ResponseEntity<?> checkAuth(String authToken){
//        if (userRepository.findUserByToken(authToken).isEmpty()){
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//
////    GET http://localhost:8080/songsWS-momotarian/rest/songs
//    @GetMapping(value = "/songs",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getAllSongs(@RequestHeader("Authorization") String authToken) throws IOException {
//        if( !authService.isTokenValid(authToken)) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
//        return new ResponseEntity<>("Yes, this worked", HttpStatus.OK);
//        //return songRepository.findAll();
//    }
//
//
////    @GetMapping(value = "/songs",
////            produces = MediaType.APPLICATION_JSON_VALUE)
////    public ResponseEntity<?> getAllSongs() throws IOException {
////        Song s = new Song();
////        s.setId(1);
////        s.setArtist("Me");
////        s.setLabel("My");
////        s.setReleased(2021);
////        s.setTitle("Mine");
////        return new ResponseEntity<>(s, HttpStatus.OK);
////    }
//
//    //GET http://localhost:8080/songsWS-momotarian/rest/songs/1
//    @GetMapping(value = "/songs/{id}",
//                produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getSong(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) throws IOException {
//        if( !authService.isTokenValid(authToken)) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
//       // return songRepository.findById(id)
//        //        .orElseThrow(() -> new ResourceNotFoundException("Song","id", id));
//        return new ResponseEntity<>(songRepository.findById(id), HttpStatus.OK);
//    }
//
//    //POST http://localhost:8080/songsWS-momotarian/rest/songs
//    //body : {"id":2,"title":"Covid","artist":"Someone","label":"RCA","released":2020}
//    @PostMapping(value ="/songs",
//            consumes = {MediaType.APPLICATION_JSON_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<?> addSong(@RequestBody Song newSong, @RequestHeader("Authorization") String authToken){
//        if( !authService.isTokenValid(authToken)) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
//        if (newSong.getTitle() != null) {
//            Song s = songRepository.save(newSong);
//            return new ResponseEntity<>("Location : " + "http://localhost:8080/songsWS-momotarian/rest/songs/" + s.getId(), HttpStatus.CREATED);
//        }
//        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//    }
//
//    @PutMapping(value = "/songs/{id}",
//            consumes = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<?> updateSong(@RequestBody Song newSong,
//                           @PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken){
//        if( !authService.isTokenValid(authToken)) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
//        Song song = songRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
//        if (song != null) {
//            song.setId(newSong.getId());
//            song.setLabel(newSong.getLabel());
//            song.setArtist(newSong.getArtist());
//            song.setReleased(newSong.getReleased());
//            song.setTitle(newSong.getTitle());
//            Song updatedSong = songRepository.save(song);
//            return new ResponseEntity<>(updatedSong, HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteSong(@PathVariable (value = "id") Integer id, @RequestHeader("Authorization") String authToken){
//        if( !authService.isTokenValid(authToken)) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
//        Song song = songRepository.findById(id)
//                .orElseThrow(()-> new ResourceNotFoundException("Song", "id" , id));
//        songRepository.delete(song);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping(value="/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<Song> getSong(@PathVariable(value="id") Integer id,
                                         @RequestHeader(name = "Authorization", required = false) String token) {
        if(!authService.isTokenValid(token) || token==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Song song;
        try{
            song = songService.getSong(id);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @GetMapping(produces = {"application/json", "application/xml"})
    public ResponseEntity<ArrayList<Song>> allSongs(
            @RequestHeader(name = "Authorization", required = false) String token) {
        if(token==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!authService.isTokenValid(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ArrayList songsList;
        try {
            songsList = songService.findAll();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(songsList, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity postSong(@RequestBody String jsonBody,
                                   @RequestHeader(name = "Authorization", required = false) String token) {
        if(!authService.isTokenValid(token)|| token==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        try {
            Song song = gson.fromJson(jsonBody, Song.class);
            if(song.getTitle()==null){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
            int id = songService.postSong(song);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Location", "http://localhost:8080/songs/"+String.valueOf(id));
            return new ResponseEntity(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity((HttpStatus.BAD_REQUEST));
        }
    }

    @PutMapping(value="/{id}", consumes ="application/json")
    public ResponseEntity putSong(@RequestBody String jsonBody, @PathVariable (value = "id") Integer id,
                                  @RequestHeader(name = "Authorization", required = false) String token) {
        if(!authService.isTokenValid(token)|| token==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            Song song = gson.fromJson(jsonBody, Song.class);
            songService.putSong(id,song);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }catch (JsonSyntaxException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity deleteSong(@PathVariable (value="id") Integer id,
                                     @RequestHeader(name = "Authorization", required = false) String token) {

        if(!authService.isTokenValid(token)|| token==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            songService.deleteSong(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }


    public void setServices(ISongService mockSongsService, AuthService mockAuthService) {
        this.authService = mockAuthService;
        this.songService = mockSongsService;
    }
}
