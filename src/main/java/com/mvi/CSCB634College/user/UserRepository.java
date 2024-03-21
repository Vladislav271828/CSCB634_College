package com.mvi.CSCB634College.user;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByTokens_Token(String token);

    @Transactional
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


}
