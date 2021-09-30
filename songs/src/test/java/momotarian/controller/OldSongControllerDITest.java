//package momotarian.controller;
////package de.htw.ai.momotarian.controller;
//
//import de.htw.ai.momotarian.dao.TestSongDAO;
//import de.htw.ai.momotarian.model.Song;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Map;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class SongControllerDITest {
//
//    private MockMvc mockMvc;
//    private TestSongDAO songDAO;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup (
//                new SongControllerDI(new TestSongDAO())).build();
//    }
//
//    @Test
//        // GET /songs/2
//    void getSongShouldReturn404ForNonExistingId() throws Exception {
//        mockMvc.perform(get("/songs/2"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//        // GET /song/-1
//    void getSongShouldReturn400ForInvalidId() throws Exception {
//        mockMvc.perform(get("/songs/-1"))
//                .andExpect(status().isBadRequest());
//    }
//
////    @Test
////        // GET /song/1
////    void getSongShouldReturn200ForValidId() throws Exception {
////        Song song = Song.builder()
////                .withId(1)
////                .withTitle("All of me")
////                .withArtist("John Legend")
////                .withLabel("Something")
////                .withReleased(2016)
////                .build();
////        songDAO.addSong(song);
////        mockMvc.perform(get("/songs/1"))
////                .andExpect(status().isOk());
////    }
//
//    @Test
//        // GET /songs
//    void getSongShouldReturn200WhenThereAreSongs() throws Exception {
//        mockMvc.perform(get("/songs"))
//                .andExpect(status().isOk());
//    }
//
////    @Test
////        // POST /song
////    void postValidSongShouldReturn201() throws Exception {
////
////        mockMvc.perform(MockMvcRequestBuilders.post("/songs")
////                        .contentType(MediaType.APPLICATION_JSON_VALUE)
////                        .content("{\"id\":2,\"title\":\"Covid\",\"artist\":\"Someone\",\"label\":\"RCA\",\"released\":2020}"))
////                .andExpect(status().isCreated())
////                .andExpect(content().string("Location : http://localhost:8080/songsWS-momotarian/rest/songs/1"));
////
////    }
//
////    @Test
////        // GET /song/-1
////    void putSongShouldReturn400ForInvalidId() throws Exception {
////        mockMvc.perform(put("/songs/-1")
////                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"id\":2,\"title\":\"Covid\",\"artist\":\"Someone\",\"label\":\"RCA\",\"released\":2020}"))
////                .andExpect(status().isBadRequest());
////    }
//
//
//}