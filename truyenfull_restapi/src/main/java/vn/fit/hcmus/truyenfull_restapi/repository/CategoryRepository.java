package vn.fit.hcmus.truyenfull_restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fit.hcmus.truyenfull_restapi.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByUrlname(String urlname);
    Category findByName(String name);
}
