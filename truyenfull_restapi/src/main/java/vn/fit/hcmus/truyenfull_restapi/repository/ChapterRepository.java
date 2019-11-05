package vn.fit.hcmus.truyenfull_restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fit.hcmus.truyenfull_restapi.model.Chapter;
import vn.fit.hcmus.truyenfull_restapi.model.Comic;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    Chapter findByName(String name);
}
