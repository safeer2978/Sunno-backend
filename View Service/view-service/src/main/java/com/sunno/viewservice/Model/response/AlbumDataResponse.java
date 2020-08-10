package com.sunno.viewservice.Model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sunno.viewservice.Model.persistence.Album;
import com.sunno.viewservice.Model.persistence.Artist;
import com.sunno.viewservice.Model.persistence.Track;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AlbumDataResponse implements Serializable {
    Album album;
    List<Track> tracks;
}
