package com.sunno.viewservice.Model.persistence.Repository;

import com.sunno.viewservice.Model.persistence.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist,Long> {

    @Query("FROM Playlist WHERE user_id = :user_id")
    List<Playlist> findByUser(@Param("user_id") long user_id);
}
