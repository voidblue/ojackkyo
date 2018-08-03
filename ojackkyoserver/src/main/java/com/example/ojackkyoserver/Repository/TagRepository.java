package com.example.ojackkyoserver.Repository;

import com.example.ojackkyoserver.Model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRespository extends JpaRepository<Tag, Integer> {
    List<Tag> findByName(String name);
    boolean existsByName(String name);
}
