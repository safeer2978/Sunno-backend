package com.sunno.viewservice.Model.response;

import com.sunno.viewservice.Model.persistence.Album;
import com.sunno.viewservice.Model.persistence.Artist;
import com.sunno.viewservice.Model.persistence.Genre;
import com.sunno.viewservice.Model.persistence.Playlist;
import lombok.Data;

import java.util.List;

@Data
public class MetaDataResponse {
    List<Genre> genre;
    List<Artist> artists;
    List<Album> albums;
    List<Playlist> playlists;
}
