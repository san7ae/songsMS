package de.htw.ai.momotarian.controller;

import de.htw.ai.momotarian.model.Song;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value="/songsWS-momotarian/rest/new")
public class YoutubeLinkController {

        //GET http://localhost:8080/songsWS-momotarian/rest/new
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getYTLink(@RequestParam(value="artistname") String fn) throws IOException {

        return new ResponseEntity<>("https://www.youtube.com/results?search_query="+fn, HttpStatus.OK);

    }
}
