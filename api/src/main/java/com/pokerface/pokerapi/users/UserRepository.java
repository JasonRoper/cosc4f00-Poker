package com.pokerface.pokerapi.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the database repository of the User objects, each method written is a JPQL database wired up to MySQL. \
 *
 * Each method represents a java abstraction that is converted by CrudRepository/Spring into a JPQL query
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    /**
     * Searches for a user matching the String provided, this method ignores case. Used to see if user exists during registration
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
}
