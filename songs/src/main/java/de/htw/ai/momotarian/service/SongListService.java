package de.htw.ai.momotarian.service;

import de.htw.ai.momotarian.model.Song;
import de.htw.ai.momotarian.model.SongList;
import de.htw.ai.momotarian.repo.SongListRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

@Service
public class SongListService implements ISongListService {

    @Autowired
    ISongService songService;

    @Autowired
    SongListRepository songListRepository;

    @Override
    public SongList getSongList(int id) throws NotFoundException {
        try{
            SongList songList = songListRepository.findById(id).get();
            return songList;
        }catch(Exception e){
            throw new NotFoundException("SongList with given Id doesn't exist.");
        }
    }

    @Override
    public void deleteSongList(Integer id) throws NotFoundException {
        try{
            songListRepository.delete(getSongList(id));
        }catch(Exception e){
            throw new NotFoundException("SongList with given Id doesn't exist.");
        }
    }

    @Override
    public Set<SongList> getSongListSetByUser(String userId, boolean privateAudience) throws NotFoundException {
        try{
            ArrayList<SongList> allSongList = (ArrayList<SongList>) songListRepository.findAll();
            Set<SongList> userSongList = new HashSet<>();
            for (SongList s : allSongList){
                if(s.getOwnerId().equals(userId)&&!(s.getIsPrivate()==true&&privateAudience==false)){
                    userSongList.add(s);
                }
            }
            return userSongList;
        }catch(Exception e){
            throw new NotFoundException("SongList not found.");
        }
    }

    @Override
    public void deleteSongInSongList(Song song) {
        ArrayList<SongList> songLists = (ArrayList<SongList>) songListRepository.findAll();
        for (SongList s : songLists){
            for(int i = 0; i<s.getSongList().size(); i++){
                Song so = s.getSongList().get(i);
                if(song.getId()==so.getId()){
                    s.getSongList().remove(i);
                }
            }
            songListRepository.save(s);
        }
    }

    @Override
    public int postSongList(SongList songList, String token) throws NotFoundException {
        //TODO : Implement
        return 0;
    }

    @Override
    public void updateSongInSongList(SongList songList) throws NotFoundException {
        List<Song> sList = songList.getSongList();
        if(songService.areSongsValid(sList)){
            try{
                songListRepository.findById(songList.getSongListId());
                songListRepository.save(songList);
            }catch(Exception e){
                throw new NotFoundException("Songliste nicht gefunden!");
            }
        }else{
            throw new NotFoundException("Diese Songs sind teilweise nicht in der DB!");
        }
    }

    public void setRepositoryAndService(SongListRepository mockSongListRepository, SongService mockSongService) {
        this.songListRepository = mockSongListRepository;
        this.songService = mockSongService;
    }
}
