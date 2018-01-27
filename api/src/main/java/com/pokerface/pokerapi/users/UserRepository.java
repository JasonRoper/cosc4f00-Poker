package com.pokerface.pokerapi.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);
}
