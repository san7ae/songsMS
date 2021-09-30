package momotarian.service;

import de.htw.ai.momotarian.model.Song;
import de.htw.ai.momotarian.model.SongList;
import de.htw.ai.momotarian.repo.SongRepository;
import de.htw.ai.momotarian.service.SongListService;
import de.htw.ai.momotarian.service.SongService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class SongServiceTest {
    //test-object
    SongService songsService;
    //mock-objects
    SongRepository mockSongsRepository;
    SongListService mockSongListService;
    //Songs in DB
    Song songInDb1;
    Song songInDb2;
    Song songInDb3;
    //New Songs
    Song newSong1;
    Song newSong2NoTitle;
    //Not Valid Song
    Song notValidSong;
    //SongList
    SongList songList;
    //List with Songs
    ArrayList<Song> listWithSongs;
    ArrayList<Song> listWithNotValidSongs;
    //ids
    Integer existingId;
    Integer notExistingId;

    @BeforeEach
    public void setUp(){
        songsService = new SongService();
        mockSongsRepository = Mockito.mock(SongRepository.class);
        mockSongListService = Mockito.mock(SongListService.class);
        songsService.setRepository(mockSongsRepository, mockSongListService);

        songInDb1 = new Song();
        songInDb1.setArtist("Artist1");
        songInDb1.setLabel("Label1");
        songInDb1.setReleased(1);
        songInDb1.setTitle("Title1");
        songInDb1.setId(1);

        songInDb2 = new Song();
        songInDb2.setArtist("Artist2");
        songInDb2.setLabel("Label2");
        songInDb2.setReleased(2);
        songInDb2.setTitle("Title2");
        songInDb2.setId(2);

        songInDb3 = new Song();
        songInDb3.setArtist("Artist3");
        songInDb3.setLabel("Label3");
        songInDb3.setReleased(3);
        songInDb3.setTitle("Title3");
        songInDb3.setId(3);

        newSong1 = new Song();
        newSong1.setTitle("TitleNew");
        newSong1.setArtist("ArtistNew");
        newSong1.setReleased(4);
        newSong1.setLabel("NewLable");
        newSong1.setId(4);

        newSong2NoTitle = new Song();
        newSong2NoTitle.setLabel("NewLableNoTitle");
        newSong2NoTitle.setReleased(5);
        newSong2NoTitle.setArtist("NewArtistNoTitle");

        notValidSong = new Song();
        notValidSong.setArtist("NotValidArtist");
        notValidSong.setTitle("NotValidTitle");
        notValidSong.setReleased(6);
        notValidSong.setLabel("NotValidLable");

        songList = new SongList();
        ArrayList<Song> songListSongList = new ArrayList<>();
        songList.setSongList(songListSongList);
        songList.getSongList().add(songInDb1);

        listWithSongs = new ArrayList<Song>();
        listWithSongs.add(songInDb1);
        listWithSongs.add(songInDb2);
        listWithSongs.add(songInDb3);

        listWithNotValidSongs = new ArrayList<Song>();
        listWithNotValidSongs.add(notValidSong);

        existingId = 1;
        notExistingId = 100;

        when(mockSongsRepository.findAll()).thenReturn(listWithSongs);
        when(mockSongsRepository.findById(existingId)).thenReturn(Optional.of(songInDb1));
        when(mockSongsRepository.findById(2)).thenReturn(Optional.of(songInDb2));
        when(mockSongsRepository.findById(3)).thenReturn(Optional.of(songInDb3));
        when(mockSongsRepository.findById(notExistingId)).thenThrow(EntityNotFoundException.class);
        when(mockSongsRepository.save(newSong2NoTitle)).thenThrow(EntityNotFoundException.class);
        when(mockSongsRepository.findById(notValidSong.getId())).thenThrow(EntityNotFoundException.class);
        doThrow(EntityNotFoundException.class).when(mockSongsRepository).deleteById(notExistingId);
    }

    //findAll-Test
    @Test
    public void findAllTest1Good() throws NotFoundException {
        assertTrue(songsService.findAll().size()==listWithSongs.size());
        assertTrue(songsService.findAll().get(0).getReleased()==listWithSongs.get(0).getReleased());
        assertTrue(songsService.findAll().get(1).getReleased()==listWithSongs.get(1).getReleased());
        assertTrue(songsService.findAll().get(1).getArtist().equals(listWithSongs.get(1).getArtist()));
        assertTrue(songsService.findAll().get(2).getTitle().equals(listWithSongs.get(2).getTitle()));
    }
    @Test public void findAllTest2NotFound(){
        when(mockSongsRepository.findAll()).thenThrow(EntityNotFoundException.class);
        try{
            songsService.findAll();
        }catch(NotFoundException e){
            assertTrue(e.getMessage().equals("No songs"));
        }
    }
    //getSong-Test
    @Test public void getSongTest1Good() throws NotFoundException {
        assertTrue(songsService.getSong(existingId).getArtist().equals(songInDb1.getArtist()));
        assertTrue(songsService.getSong(existingId).getTitle().equals(songInDb1.getTitle()));
        assertTrue(songsService.getSong(existingId).getReleased()==songInDb1.getReleased());
    }
    @Test public void getSongTest2NotFound(){
        try {
            songsService.getSong(notExistingId);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("No Song with this Id"));
        }
    }
    //postSong-Test
    @Test public void postSongTest1Good() throws Exception {
        assertTrue(songsService.postSong(newSong1)==4);
    }
    @Test public void postSongTest2NoTitle(){
        try{
            songsService.postSong(newSong2NoTitle);
        }catch (Exception e){
            assertTrue(e.getMessage().equals("Title on null is not allowed!"));
        }
    }
    //putSong-Test
    @Test public void putSongTest1Good() throws NotFoundException {
        assertTrue(songInDb1.getArtist().equals("Artist1"));
        songInDb1.setArtist("Artist1put");
        songsService.putSong(1,songInDb1);
        assertTrue(songInDb1.getArtist().equals("Artist1put"));
    }
    @Test public void putSongTest2NoTitle(){
        newSong2NoTitle.setId(5);
        try{
            songsService.putSong(5,newSong2NoTitle);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("The given parameters are not valid."));
        }
    }
    @Test public void putSongTest3NotMatchingId(){
        try{
            songsService.putSong(2,songInDb1);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("The given parameters are not valid."));
        }
    }
    @Test public void putSongTest4IdNotFound(){
        newSong1.setId(notExistingId);
        try{
            songsService.putSong(notExistingId,newSong1);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("Song with the given id doesn't exist."));
        }
    }
    //deleteSong-Test
    @Test public void deleteSongTest1Good() throws NotFoundException {
        songsService.deleteSong(existingId);
    }
    @Test public void deleteSongTest2IdNotFound(){
        try{
            songsService.deleteSong(notExistingId);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("Song with the given id doesn't exist."));
        }
    }
    //areSongsValid-Test
    @Test public void areSongsValidTest1Valid(){
        assertTrue(songsService.areSongsValid(listWithSongs));
    }
    @Test public void areSongsValidTest2NotValid(){
        listWithSongs.add(newSong1);
        assertTrue(!songsService.areSongsValid(listWithSongs));
    }
}
