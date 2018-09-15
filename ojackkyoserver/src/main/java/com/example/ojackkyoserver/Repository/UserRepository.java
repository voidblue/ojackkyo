package com.example.ojackkyoserver.Repository;

import com.example.ojackkyoserver.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    Optional<User> findById(Integer id);
    Optional<User> findByUid(String uid);
    boolean existsByUid(String uid);
    boolean existsByNickname(String nickname);

    User findByNickname(String authorsNickname);
}
