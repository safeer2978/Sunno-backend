package com.sunno.viewservice.Model.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="artist")
@JsonIgnoreProperties
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String image_url;


    @OneToMany(mappedBy = "artist")
            @JsonManagedReference
    List<Album> albums;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
            @JoinTable(name = "artist_track",
                joinColumns = @JoinColumn(name="artist_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id"))
            @JsonManagedReference                                                                                       //IMPT FOR SOLVING STACK OVERFLOW ERROR
    List<Track> tracks;

    public void addTrack(Track track){
        tracks.add(track);
        track.getArtists().add(this);
    }

}
