package de.htw.ai.momotarian.repo;

import de.htw.ai.momotarian.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Qualifier("users")
@Repository
public interface UserRepository extends CrudRepository<User, String> {

    @Query(value = "SELECT * FROM depd2ub891h5eu.public.User WHERE userid = ?1", nativeQuery = true)
    List<User> findUserByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE depd2ub891h5eu.public.User SET token = ?2 WHERE userid = ?1", nativeQuery = true)
    void updateUser(String userId, String token);

    @Query(value = "SELECT * FROM depd2ub891h5eu.public.User WHERE token = ?1", nativeQuery = true)
    List<User> findUserByToken(String token);
}
