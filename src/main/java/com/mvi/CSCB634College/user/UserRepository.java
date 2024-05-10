package com.mvi.CSCB634College.user;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u inner join u.tokens tokens where tokens.token = ?1")
    Optional<User> findUserByToken(String token);

    Optional<User> findByTokens_Token(String token);

    @Transactional
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    boolean existsByEmail(String email);


    Arrays findByEmailIgnoreCase(String email);
}
