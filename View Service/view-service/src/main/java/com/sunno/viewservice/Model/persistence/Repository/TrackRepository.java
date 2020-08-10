package com.sunno.viewservice.Model.persistence.Repository;

import com.sunno.viewservice.Model.persistence.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track,String> {


    @Query("FROM Track WHERE album_id = :album_id")
    public List<Track> findAllByAlbumId(@Param("album_id")long artist_id);



}
