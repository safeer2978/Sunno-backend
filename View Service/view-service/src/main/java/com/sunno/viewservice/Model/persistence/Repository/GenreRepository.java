package com.sunno.viewservice.Model.persistence.Repository;

import com.sunno.viewservice.Model.persistence.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("from Genre where name = :name")
    Genre findByName(@Param("name") String name);
}
