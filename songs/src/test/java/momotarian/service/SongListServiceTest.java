package momotarian.service;

import de.htw.ai.momotarian.model.Song;
import de.htw.ai.momotarian.model.SongList;
import de.htw.ai.momotarian.repo.SongListRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class SongListServiceTest {

    //test-Object
    SongListService songListService;
    //mock-Objects
    SongListRepository mockSongListRepository;
    SongService mockSongsService;
    //Songs in DB
    Song songInDb1;
    Song songInDb2;
    Song songInDb3;
    Song songNotInDb;
    //SongList
    SongList songList;
    SongList songListWithBadSong;
    //user
    String ownerUser;
    String foreignUser;
    //ids
    int existingId;
    int notExistingId;
    //listOfSongList
    ArrayList<SongList> listOfSongLists;

    @BeforeEach
    public void setUp(){
        songListService = new SongListService();
        mockSongListRepository = Mockito.mock(SongListRepository.class);
        mockSongsService = Mockito.mock(SongService.class);
        songListService.setRepositoryAndService(mockSongListRepository,mockSongsService);

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

        songNotInDb = new Song();
        songNotInDb.setArtist("Artist4");
        songNotInDb.setLabel("Label4");
        songNotInDb.setReleased(4);
        songNotInDb.setTitle("Title4");
        songNotInDb.setId(4);

        ownerUser = "owner";
        foreignUser = "notOwner";

        songList = new SongList();
        ArrayList<Song> songListSongList = new ArrayList<>();
        songList.setSongList(songListSongList);
        songList.getSongList().add(songInDb1);
        songList.getSongList().add(songInDb2);
        songList.setOwnerId(ownerUser);
        songList.setSongListId(1);
        songList.setIsPrivate(true);
        songList.setName("songList1");

        songListWithBadSong = new SongList();
        ArrayList<Song> songListSongListWithBadSong = new ArrayList<>();
        songListWithBadSong.setSongList(songListSongListWithBadSong);
        songListWithBadSong.getSongList().add(songNotInDb);
        songListWithBadSong.setOwnerId(ownerUser);
        songListWithBadSong.setSongListId(2);
        songListWithBadSong.setIsPrivate(true);
        songListWithBadSong.setName("songListSongNotInDB");

        listOfSongLists = new ArrayList<>();
        listOfSongLists.add(songList);

        existingId = songList.getSongListId();
        notExistingId = 100;

        when(mockSongsService.areSongsValid(songList.getSongList())).thenReturn(true);
        when(mockSongsService.areSongsValid(songListWithBadSong.getSongList())).thenReturn(false);
        when(mockSongListRepository.findById(existingId)).thenReturn(Optional.of(songList));
        when(mockSongListRepository.findById(notExistingId)).thenThrow(EntityNotFoundException.class);
        doThrow(EntityNotFoundException.class).when(mockSongListRepository).delete(songListWithBadSong);
        when(mockSongListRepository.findAll()).thenReturn(listOfSongLists);

    }
    //postSong-test
//    @Test
//    public void postSongListTest1Good() throws NotFoundException {
//        assertTrue(songListService.postSongList(songList, ownerUser)==songList.getSongListId());
//    }
    @Test public void postSongListTest2BadSongs(){
        try{
            songListService.postSongList(songListWithBadSong,ownerUser);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("Diese Songs sind teilweise nicht in der DB!"));
        }
    }
    //getsongList-test
    @Test public void getSongListTest1Good() throws NotFoundException {
        assertTrue(songListService.getSongList(existingId).getName().equals(songList.getName()));
        assertTrue(songListService.getSongList(existingId).getOwnerId().equals(songList.getOwnerId()));
        assertTrue(songListService.getSongList(existingId).getSongListId()==songList.getSongListId());
    }
    @Test public void getSongListTest2NotFound(){
        try{
            songListService.getSongList(notExistingId);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("SongList with given Id doesn't exist."));
        }
    }
    //deleteSongList
    @Test public void deleteSongListTest1Good() throws NotFoundException {
        songListService.deleteSongList(existingId);
        Mockito.verify(mockSongListRepository).delete(songList);
    }
    @Test public void deleteSongListTest2NotFound(){
        try{
            songListService.deleteSongList(notExistingId);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("SongList with given Id doesn't exist."));
        }
    }
    //getSongListByUser
    @Test public void getSongListByUserTest1Good() throws NotFoundException {
        assertTrue(songListService.getSongListSetByUser(ownerUser,true).contains(songList));
    }
    @Test public void getSongListByUserTest2NoAccess() throws NotFoundException {
        assertTrue(songListService.getSongListSetByUser(foreignUser, false).isEmpty());
    }
    //updateSongList
    @Test public void updateSongListTest1Good() throws NotFoundException {
        songList.getSongList().add(songInDb3);
        songListService.updateSongInSongList(songList);
        Mockito.verify(mockSongListRepository).save(songList);
    }
    @Test public void updateSongListTest2NotFound(){
        try{
            songList.setSongListId(notExistingId);
            songListService.updateSongInSongList(songList);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("Songliste nicht gefunden!"));
        }
    }
    @Test public void updateSongListTest3BadSongs(){
        try{
            songListWithBadSong.setSongListId(existingId);
            songListService.updateSongInSongList(songListWithBadSong);
        }catch(Exception e){
            assertTrue(e.getMessage().equals("Diese Songs sind teilweise nicht in der DB!"));
        }
    }
    //deleteSongFromSongList
    @Test public void deleteSongFromSonglistTest1Good(){
        songListService.deleteSongInSongList(songInDb1);
        Mockito.verify(mockSongListRepository).save(any());
    }
}
