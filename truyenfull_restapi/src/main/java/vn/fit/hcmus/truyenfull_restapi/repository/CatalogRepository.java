package vn.fit.hcmus.truyenfull_restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fit.hcmus.truyenfull_restapi.model.Catalog;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog,Long> {
    Catalog findByUrlname(String urlName);
}
