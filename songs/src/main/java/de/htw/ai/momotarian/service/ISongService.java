package de.htw.ai.momotarian.service;

import de.htw.ai.momotarian.model.Song;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public interface ISongService {

    Song getSong(Integer id) throws NotFoundException;

    int postSong(Song song) throws Exception;

    void deleteSong(Integer id) throws NotFoundException;

    void putSong(Integer id, Song song) throws NotFoundException;

    boolean areSongsValid(List<Song> songs);

    ArrayList<Song> findAll() throws NotFoundException;
}
