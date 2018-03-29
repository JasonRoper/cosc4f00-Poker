package com.pokerface.pokerapi.users;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * This is the database repository of the User objects, each method written is a JPQL database wired up to MySQL. \
 *
 * Each method represents a java abstraction that is converted by CrudRepository/Spring into a JPQL query
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    /**
     * Searches for a user matching the String provided,
     * this method ignores case. Used to see if user exists during registration
     * @param username string, username being searched
     * @return a boolean indicating if that user was found
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Checks if an email exists ignoring case. Used during registration
     * @param email String indicating email being checked
     * @return boolean indicating if email ws found in the database.
     */
    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.connection = com.pokerface.pokerapi.users.User$ConnectionStatus.CONNECTED")
    long countConnected();


    @Query("SELECT u FROM User u ORDER BY u.rating DESC")
    Stream<User> orderedByRating();

    @Query("SELECT u FROM User u ORDER BY u.username ASC")
    Stream<User> orderedByName();

    @Query("SELECT u FROM User u ORDER BY u.money DESC")
    Stream<User> orderedByMoney();
}
