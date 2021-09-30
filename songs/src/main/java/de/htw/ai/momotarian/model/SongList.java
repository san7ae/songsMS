package de.htw.ai.momotarian.model;

//import org.hibernate.mapping.Set;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "songList")
public class SongList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int songListId;
    private String ownerId;
    private String name;
    private boolean isPrivate;
    private List<Song> songList;


    public SongList() {
    }
    public SongList(SongList.Builder builder) {
        this.songListId = songListId;
        this.ownerId=ownerId;
        this.name=name;
        this.isPrivate=isPrivate;
        this.songList=songList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getSongListId() {
        return songListId;
    }

    @Column(name="ownerId")
    public String getOwnerId() {
        return ownerId;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    @Column(name="isPrivate")
    public boolean getIsPrivate() {
        return isPrivate;
    }

    @ManyToMany
    public List<Song> getSongList() {
        return songList;
    }

    public void setSongListId(int id) {
        this.songListId =id;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public String toString() {
        return null;
    }

    /**
     * Creates builder to build {@link SongList}.
     * @return created builder
     */
    public static SongList.Builder builder() {
        return new SongList.Builder();
    }

    /**
     * Builder to build {@link SongList}.
     */
    public static final class Builder {
        private int songListId;
        private String ownerId;
        private String name;
        private String isPrivate;
        private Set<Song> songList;

        private Builder() {
        }

        public SongList.Builder withId(int id) {
            this.songListId = id;
            return this;
        }


        public SongList.Builder withOwnerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public SongList.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public SongList.Builder withIsPrivate(String isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public SongList.Builder withSongList(Set<Song> songList) {
            this.songList = songList;
            return this;
        }

        public SongList build() {
            return new SongList(this);
        }
    }
}
