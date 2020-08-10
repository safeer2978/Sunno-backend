package com.sunno.viewservice.Model.persistence.Repository;

import com.sunno.viewservice.Model.request.MusicData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Repository  {

    @Query("SELECT DISTINCT album FROM MusicData WHERE artist=:artist")
    List<String> findAlbums(@Param("artist") String artist);

    @Query("SELECT DISTINCT artist FROM MusicData")
    List<String> getArtists();

    @Query("SELECT DISTINCT genre FROM MusicData")
    List<String> getGenre();

    @Query("SELECT DISTINCT artist FROM MusicData WHERE genre = :genre")
    List<String> getArtists(@Param("genre") String genre);

    @Query("FROM MusicData WHERE album = :album")
    List<MusicData> getTracks(@Param("album") String album);

    @Query("FROM MusicData WHERE artist = :artist")
    List<MusicData> getTracksByArtist(@Param("artist") String artist);


    @Query("FROM MusicData WHERE genre = :genre")
    List<MusicData> getTracksByGenre(@Param("genre") String genre);
}
