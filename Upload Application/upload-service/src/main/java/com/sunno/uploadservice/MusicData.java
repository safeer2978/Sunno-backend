package com.sunno.uploadservice;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class MusicData {
    List<String> artistNames;
    String albumName;
    String genre;
    String albumArtist;
    String trackNumber;
    String trackTitle;

    String artist_url;
    String album_url;

    String track_id;

    String file_path;

    String genre_url;

    public boolean consistNull(){
        if(albumName == null ||
            genre == null ||
            artistNames == null ||
            trackNumber == null ||
            trackTitle==null ||
            albumArtist == null)
            return true;
        return false;
    }

}
