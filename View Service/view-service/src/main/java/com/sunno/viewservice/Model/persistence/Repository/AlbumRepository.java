package com.sunno.viewservice.Model.persistence.Repository;

import com.sunno.viewservice.Model.persistence.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {


    @Query("FROM Album WHERE artist_id = :artist_id")
    public List<Album> findAllByArtistId(@Param("artist_id")long artist_id);

    @Query("FROM Album WHERE combination = :combi")
    Album findbyCombination(@Param("combi")String combination);
}
