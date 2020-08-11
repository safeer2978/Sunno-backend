package com.sunno.uploadservice;

import lombok.Data;

import java.util.List;

@Data
public class MusicDataRequest {
    List<MusicData> musicDataList;
}
