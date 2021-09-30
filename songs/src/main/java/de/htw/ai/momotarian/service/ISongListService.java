package de.htw.ai.momotarian.service;

import de.htw.ai.momotarian.model.Song;
import de.htw.ai.momotarian.model.SongList;
import javassist.NotFoundException;

import java.util.Set;

public interface ISongListService {

    SongList getSongList(int id) throws NotFoundException;

    void deleteSongList(Integer id) throws NotFoundException;

    Set<SongList> getSongListSetByUser(String userId, boolean privateAudience) throws NotFoundException;

    void deleteSongInSongList(Song song);

    int postSongList(SongList songList, String token) throws NotFoundException;

    void updateSongInSongList(SongList songList) throws NotFoundException;
}
