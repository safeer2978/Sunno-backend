package com.sunno.viewservice.Model.request;

import com.sunno.viewservice.Model.persistence.Track;
import lombok.Data;

import java.util.List;

@Data
public class CreatePlaylistRequest {

    String name;
    long user_id;
    List<String> track_ids;

}
