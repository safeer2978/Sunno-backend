package com.sunno.viewservice.Model.request;


import lombok.Data;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;


@Data
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


}
