package com.sunno.viewservice.Model.response;

import com.sunno.viewservice.Model.persistence.Album;
import com.sunno.viewservice.Model.persistence.Artist;
import com.sunno.viewservice.Model.persistence.Track;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ArtistDataResponse {
    Artist artist;

    Map<Album, List<Track>> albumData;
}

