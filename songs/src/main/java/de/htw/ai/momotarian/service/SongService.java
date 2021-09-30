package de.htw.ai.momotarian.service;

import de.htw.ai.momotarian.model.Song;
import de.htw.ai.momotarian.repo.SongRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongService implements ISongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ISongListService songListService;

    @Override
    public Song getSong(Integer id) throws NotFoundException {
        try { return songRepository.findById(id).get(); }
        catch (Exception e) { throw new NotFoundException("No Song with this Id"); }
    }

    @Override
    public int postSong(Song song) throws Exception {
        try{
            songRepository.save(song);
            return song.getId();
        }catch (Exception e){
            throw new Exception("Title on null is not allowed!");
        }
    }

    @Override
    public void deleteSong(Integer id) throws NotFoundException {
        try{
            songListService.deleteSongInSongList(songRepository.findById(id).get());
            songRepository.deleteById(id);
        }catch(Exception e){
            e.printStackTrace();
            throw new NotFoundException("Song with the given id doesn't exist.");
        }
    }

    @Override
    public void putSong(Integer id, Song newSong) throws NotFoundException {
        if(newSong.getId()!=id || newSong.getTitle()==null){
            throw new InvalidParameterException("The given parameters are not valid.");
        }
        try{
            Song oldSong = songRepository.findById(id).get();
            oldSong.setArtist(newSong.getArtist());
            oldSong.setTitle(newSong.getTitle());
            oldSong.setLabel(newSong.getLabel());
            oldSong.setReleased(newSong.getReleased());

            songRepository.save(oldSong);
        }catch(Exception e){
            throw new NotFoundException("Song with the given id doesn't exist.");
        }
    }

    @Override
    public boolean areSongsValid(List<Song> songList) {
        for(Song s : songList){
            try{
                if(!s.equals(songRepository.findById(s.getId()).get())){
                    return false;
                }
            }catch(Exception e){
                return false;
            }
        }
        return true;
    }

    @Override
    public ArrayList<Song> findAll() throws NotFoundException {
        try{return (ArrayList<Song>) songRepository.findAll();}
        catch(Exception e){throw new NotFoundException("No songs");}
    }

    public void setRepository(SongRepository mockSongRepository, SongListService mockSongListService){
        this.songRepository = mockSongRepository;
        this.songListService = mockSongListService;
    }
}
