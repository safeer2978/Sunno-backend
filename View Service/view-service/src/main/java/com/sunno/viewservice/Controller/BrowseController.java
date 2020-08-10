package com.sunno.viewservice.Controller;


import com.sunno.viewservice.Model.persistence.*;
import com.sunno.viewservice.Model.request.CreatePlaylistRequest;
import com.sunno.viewservice.Model.request.MusicData;
import com.sunno.viewservice.Model.request.MusicDataRequest;
import com.sunno.viewservice.Model.response.AlbumDataResponse;
import com.sunno.viewservice.Model.response.ArtistDataResponse;
import com.sunno.viewservice.Model.response.MetaDataResponse;
import com.sunno.viewservice.Model.persistence.Repository.ArtistRepository;
import com.sunno.viewservice.Service.MusicDataService;
import com.sunno.viewservice.util.JwtUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//TODO Save data from Upload Service!

@RestController
public class BrowseController {

    @Autowired
    private MusicDataService service;

    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/")
    public String isAlive(){
        return "View-Service is Alive at: "+Calendar.getInstance().getTime();
    }

    @PostMapping("/updateMusicData")
    public Map<String,List<Boolean>> updateMusicData(@RequestBody MusicDataRequest data){
        //JSONObject jsonObject = new JSONObject(data);
        //JSONArray array = new JSONArray(jsonObject.getString("data"));
        List<Boolean> statusList = new ArrayList<>();
        List<MusicData> array = data.getData();
        for(int i =0; i < array.size();i++){
            System.out.println(array.get(i));
            MusicData musicData = (MusicData) array.get(i);
            statusList.add(service.saveMusicData(musicData));
        }

        //boolean status = service.saveAll(musicDataList);
        //JSONObject returnObject = new JSONObject();
        //returnObject.put("status",statusList);
        Map<String,List<Boolean>> map =new HashMap<>();
        map.put("status",statusList);
        return map;
    }

    @GetMapping("/playlist/{user_id}")
    public List<Playlist> getUserPlaylists(@PathVariable long user_id){
        return service.getPlaylistsFromUserId(user_id);
    }

    @GetMapping("/metadata")
    public ResponseEntity<MetaDataResponse> getMetaData(){
        List<Artist> artists = service.getAllArtists();
        List<Album> albums = service.getAllAlbums();
        List<Genre> genres = service.getAllGenre();
        List<Playlist> playlists = service.getAllGeneralPlaylists();


        MetaDataResponse response = new MetaDataResponse();

        response.setAlbums(albums);
        response.setArtists(artists);
        response.setGenre(genres);
        response.setPlaylists(playlists);
        System.out.println(response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/artists")
    public List<Artist> getArtists(){
        return service.getAllArtists();
    }

    @GetMapping("/artist/{artist_id}")
    public ResponseEntity<Object> getArtistData(@PathVariable Long artist_id){
        if(service.getArtistFromId(artist_id).isPresent()) {
        ArtistDataResponse response = new ArtistDataResponse();
        Map<Album,List<Track>> albumData = new HashMap<>();
        List<Album> albums = service.getAlbumsFromArtistId(artist_id);
        for(Album album: albums){
            List<Track> tracks = service.getTracksFromAlbumId(album.getId()).get();
            albumData.put(album,tracks);
        }
            response.setAlbumData(albumData);
            response.setArtist(service.getArtistFromId(artist_id).get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else
        return new ResponseEntity<>("No Artist with given id",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/albums")
    public  List<Album> getAllAlbums(){
        return service.getAllAlbums();
    }

    @GetMapping("/album/{album_id}")
    public ResponseEntity<Object> getArtistAlbum(@PathVariable long album_id){
        if(service.getAlbumById(album_id).isPresent()){
        AlbumDataResponse albumDataResponse = new AlbumDataResponse();
        albumDataResponse.setAlbum(service.getAlbumById(album_id).get());


            albumDataResponse.setTracks(service.getTracksFromAlbumId(album_id).get());
            return new ResponseEntity<>(albumDataResponse,HttpStatus.OK);}
        else
            return new ResponseEntity<>("No Album with given ID", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/playlist/create")
    public boolean createNewPlaylist(@RequestBody CreatePlaylistRequest request){
        Optional<Playlist> playlist = Optional.ofNullable(service.createPlaylist(request));
        return playlist.isPresent();
    }

    @PostMapping("/playlist/add/{playlist_id}/{track_id}")
    public ResponseEntity<Object> addToPlaylist(@RequestBody String token, @PathVariable Long playlist_id, @PathVariable String track_id){
        JwtUtil jwtUtil = new JwtUtil();

        JSONObject jsonObject = new JSONObject(jwtUtil.extractUsername(token));

        List<Playlist> playlists = service.getPlaylistsFromUserId((jsonObject.getInt("id")));
        for(Playlist playlist: playlists){
            if(playlist.getId()==playlist_id)
                break;
            else return new ResponseEntity<>("Error: Not Playlist Owner", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.addTrackToPlaylist(playlist_id, track_id),HttpStatus.OK);

    }

    @PostMapping("/playlist/remove/{playlist_id}/{track_id}")
    public ResponseEntity<Object> removeFromPlaylist(@RequestBody String token, @PathVariable Long playlist_id, @PathVariable String track_id){
        JwtUtil jwtUtil = new JwtUtil();

        JSONObject jsonObject = new JSONObject(jwtUtil.extractUsername(token));

        List<Playlist> playlists = service.getPlaylistsFromUserId((long)jsonObject.getInt("id"));
        for(Playlist playlist: playlists){
            if(playlist.getId()==playlist_id)
                break;
            else return new ResponseEntity<>("Error: Not Playlist Owner", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.addTrackToPlaylist(playlist_id, track_id),HttpStatus.OK);
    }


}

