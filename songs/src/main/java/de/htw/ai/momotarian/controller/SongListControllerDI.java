package de.htw.ai.momotarian.controller;

import de.htw.ai.momotarian.model.SongList;
import de.htw.ai.momotarian.service.AuthService;
import de.htw.ai.momotarian.service.ISongListService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value="/songWS-momotarian/rest/songsLists")
public class SongListControllerDI {

    @Autowired
    ISongListService songListService;

    @Autowired
    AuthService authService;

    @GetMapping(value="/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity getSongListById(@PathVariable(value="id") Integer id,
                                          @RequestHeader(name = "Authorization", required = false) String token) {
        if(!authService.isTokenValid(token) || token==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SongList songList;
        try{
            songList = songListService.getSongList(id);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try{
            //wenn SongList privat, muss token zu SongList-Owner gehören
            String userId =  songList.getOwnerId();
            if(songList.getIsPrivate() &&
                    !authService.doTokenAndIdMatch(token, userId)){
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(songList, HttpStatus.OK);
    }
    @PostMapping(consumes = "application/json")
    public ResponseEntity<SongList> postSongList(@RequestBody SongList songList,
                                                 @RequestHeader(name="Authorization", required = false) String token){
        if(!authService.isTokenValid(token)|| token==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            String userId = authService.getUserIdByToken(token);
            int id = songListService.postSongList(songList, userId);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Location", "http://localhost:8080/songs/playist/"+String.valueOf(id));
            return new ResponseEntity<>(songList,headers, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(produces = {"application/json", "application/xml"})
    public ResponseEntity getSongListByUser(@RequestParam(value="userId") String userId,
                                            @RequestHeader(name = "Authorization", required = false) String token) {
        if(!authService.isTokenValid(token)|| token==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            if(!authService.isUserValid(userId)){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            boolean privateAudience = authService.doTokenAndIdMatch(token,userId);
            Set<SongList> songListSet = songListService.getSongListSetByUser(userId, privateAudience);
            return new ResponseEntity(songListSet, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(value="/{id}")
    public ResponseEntity deleteSongList(@PathVariable(value="id") Integer id,
                                         @RequestHeader(name = "Authorization", required = false) String token) {
        if (!authService.isTokenValid(token) || token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            String userId = songListService.getSongList(id).getOwnerId();
            if(authService.doTokenAndIdMatch(token,userId)){
                songListService.deleteSongList(id);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(value="/{id}")
    public ResponseEntity updateSongList(@PathVariable(value = "id") Integer id,
                                         @RequestBody SongList songList,
                                         @RequestHeader(name ="Authorization", required = false) String token){
        if (!authService.isTokenValid(token) || token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            String userId = songListService.getSongList(id).getOwnerId();
            songList.setOwnerId(userId);
            if(authService.doTokenAndIdMatch(token,userId)){
                if(songList.getSongListId()!=id){
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                songListService.updateSongInSongList(songList);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    public void setServices(ISongListService mockSongListService, AuthService mockAuthService) {
        this.authService = mockAuthService;
        this.songListService = mockSongListService;
    }

    @GetMapping(value="/owner/{songlistId}")
    public String getOwner(@PathVariable(value="songlistId") int songlistId) throws NotFoundException {
        return songListService.getSongList(songlistId).getOwnerId();
    }
    @GetMapping(value="/isprivate/{songlistId}")
    public boolean isSongListPrivate(@PathVariable(value="songlistId") int songlistId) throws NotFoundException {
        return songListService.getSongList(songlistId).getIsPrivate();
    }


}
