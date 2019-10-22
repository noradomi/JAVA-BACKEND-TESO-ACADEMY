package vn.fit.hcmus.truyenfull_restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import vn.fit.hcmus.truyenfull_restapi.model.Comic;


@Repository
public interface ComicRepositiory extends JpaRepository<Comic,Long> {
    Comic findByName(String name);
    Comic findByUrlname(String urlname);

}
