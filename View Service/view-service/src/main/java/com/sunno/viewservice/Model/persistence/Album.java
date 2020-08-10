package com.sunno.viewservice.Model.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchProfile;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="album")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    String image_url;

    @OneToOne
    Genre genre;

    @OneToMany(mappedBy = "album")
            @JsonBackReference
    Set<Track> tracks;


    @ManyToOne
            @JoinColumn(name = "artist_id")
    @JsonBackReference
    Artist artist;

    String combination;
}
