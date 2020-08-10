package com.sunno.viewservice.Model.persistence;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="playlist")
public class Playlist {

    public Playlist(long user_id, String name) {
        this.name = name;
        this.user_id = user_id;
    }
    public Playlist(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String name;

    long user_id;

    @OneToMany
            @JoinTable(name = "playlist_track",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id"))
    List<Track> tracks;
}
