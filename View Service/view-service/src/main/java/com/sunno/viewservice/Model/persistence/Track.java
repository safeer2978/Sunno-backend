package com.sunno.viewservice.Model.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@JsonIgnoreProperties
@Table(name="track")
public class Track {
    @Id
    @NonNull
    String id;

    String title;

    @ManyToOne
    @JoinColumn(name="album_id")
    @JsonBackReference
    @JsonManagedReference
    private Album album;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "artist_track",
            joinColumns = @JoinColumn(name="track_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"))
            @JsonBackReference
    List<Artist> artists;

    public Track() {

    }
}
