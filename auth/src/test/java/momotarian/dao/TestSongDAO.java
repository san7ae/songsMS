//package momotarian.dao;//package de.htw.ai.momotarian.dao;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import de.htw.ai.momotarian.model.Song;
//
//public class TestSongDAO implements ISongDAO {
//
//    private Map<Integer, Song> mySongs;
//
//    public TestSongDAO() {
//        mySongs = new ConcurrentHashMap<Integer, Song>();
//                Song song1 = Song.builder()
//                .withId(1)
//                .withTitle("All of me")
//                .withArtist("John Legend")
//                .withLabel("Something")
//                .withReleased(2016)
//                .build();
//
//        mySongs.put(song1.getId(), song1);
//    }
//
//    @Override
//    public Song getSongById (int id) {
//        Collection<Song> allSongs = mySongs.values();
//        for(Song u : allSongs) {
//            if (u.getId() == id) {return u;}
//        }
//        return null;
//    }
//
//    public void addSong(Song song) {
//        mySongs.put(song.getId(), song);
//    }
//
//    public void updateSong(Song newSong) {
//        Collection<Song> allSongs = mySongs.values();
//        for(Song song : allSongs) {
//            if (song.getId() == newSong.getId()) {
//                deleteSong(song.getId());
//                addSong(newSong);
//            }
//        }
//    }
//
//    public void deleteSong(int id) {
//        mySongs.remove(id);
//    }
//
//    @Override
//    public Map<Integer, Song> getAllSongs() {
//        return mySongs;
//    }
//
//
//}
