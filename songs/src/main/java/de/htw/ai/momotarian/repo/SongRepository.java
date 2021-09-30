package de.htw.ai.momotarian.repo;

import de.htw.ai.momotarian.model.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@Qualifier("songs")
@Repository
public interface SongRepository extends CrudRepository<Song, Integer> {
}
