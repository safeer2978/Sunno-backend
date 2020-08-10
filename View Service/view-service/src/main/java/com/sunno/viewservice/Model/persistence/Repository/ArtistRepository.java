package com.sunno.viewservice.Model.persistence.Repository;

import com.sunno.viewservice.Model.persistence.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Long> {

    @Query("from Artist where name = :name")
    Artist findByName(@Param("name") String name);
}
