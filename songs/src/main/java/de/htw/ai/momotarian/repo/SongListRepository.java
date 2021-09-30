package de.htw.ai.momotarian.repo;

import de.htw.ai.momotarian.model.SongList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Qualifier("songLists")
@Repository
public interface SongListRepository extends CrudRepository<SongList, Integer> {

    @Query(value = "SELECT * FROM d3vofpf1k8p84g.public.songlist WHERE ownerid = ?1", nativeQuery = true)
    List<SongList> findByOwnerId(String ownerid);

}
