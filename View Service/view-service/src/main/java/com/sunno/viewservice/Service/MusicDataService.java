package com.sunno.viewservice.Service;


import com.sunno.viewservice.Model.request.CreatePlaylistRequest;
import com.sunno.viewservice.Model.request.MusicData;
import com.sunno.viewservice.Model.persistence.*;
import com.sunno.viewservice.Model.persistence.Repository.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MusicDataService {


    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private GenreRepository genreRepository;


    public List<Genre> getAllGenre(){
        return genreRepository.findAll();
    }

    public List<Playlist> getAllGeneralPlaylists(){
        return playlistRepository.findByUser(0);
    }


    public List<Playlist> getPlaylistsFromUserId(long user_id){
        return playlistRepository.findByUser(user_id);
    }


    public List<Album> getAllAlbums(){
        return albumRepository.findAll();
    }

    public Optional<Album> getAlbumById(Long id){
        return albumRepository.findById(id);
    }

    public List<Artist> getAllArtists(){
        return artistRepository.findAll();
    }

    public List<Album> getAlbumsFromArtistId(Long artist_id){
        return albumRepository.findAllByArtistId(artist_id);
    }

    public Optional<List<Track>> getTracksFromAlbumId(Long album_id){
        return Optional.ofNullable(trackRepository.findAllByAlbumId(album_id));
    }

    public Optional<Artist> getArtistFromId(Long artist_id){
        return artistRepository.findById(artist_id);
    }



    public Playlist createPlaylist(CreatePlaylistRequest request){
        Playlist playlist = new Playlist();

        playlist.setName(request.getName());
        playlist.setUser_id(request.getUser_id());
        List<Track> tracks = new ArrayList<>();
        for(String track_id: request.getTrack_ids()){
            tracks.add(trackRepository.getOne(track_id));
        }
        playlist.setTracks(tracks);

        return playlistRepository.save(playlist);

    }

    public Playlist addTrackToPlaylist(long playlist_id, String track_id){
        Optional<Playlist> playlist = playlistRepository.findById(playlist_id);
        if(playlist.isPresent()){
            playlist.get().getTracks().add(trackRepository.getOne(track_id));
            playlistRepository.save(playlist.get());
        }
        return playlist.orElse(null);
    }

    @SneakyThrows
    public boolean saveMusicData(MusicData musicData){
        if(!trackRepository.findById(musicData.getTrack_id()).isPresent()){
        try {
            int j = 0;
            List<Artist> artists = new ArrayList<>();
            for (String artist_name : musicData.getArtistNames()) {
                Artist artist = artistRepository.findByName(artist_name);
                if(artist==null){
                    artist = new Artist();
                    artist.setTracks(new ArrayList<>());
                    artist.setAlbums(new ArrayList<>());
                }
                artist.setName(artist_name);
                if (j == 0) artist.setImage_url(musicData.getArtist_url());
                j += 1;
                artist=artistRepository.save(artist);
                artists.add(artist);
            }

            Album album = new Album();

            Genre genre = new Genre();
            genre.setName(musicData.getGenre());
            genre.setImg_url(musicData.getGenre_url());
            List<Genre> genres = genreRepository.findAll();
            boolean flag=true;
            System.out.println(genres+genre.getName());
            for(Genre g:genres){
                if(g.getName().equals(genre.getName()))
                {
                    flag=false;
                    break;}
            }
            if(flag)
                genreRepository.save(genre);
            else{
                genre = genreRepository.findByName(genre.getName());
            }
            album.setGenre(genre);
            album.setImage_url(musicData.getAlbum_url());
            album.setName(musicData.getAlbumName());
            album.setArtist(artists.get(0));
            album.setCombination(album.getName()+album.getArtist().getId());
            Album a = albumRepository.findbyCombination(album.getCombination());
            if(a==null)
                albumRepository.save(album);
            else
                album = a;
            Track track = new Track();
            track.setId(musicData.getTrack_id());
            track.setTitle(musicData.getTrackTitle());
            track.setAlbum(album);
            track.setArtists(artists);
             trackRepository.save(track);
             return true;
        }catch(Exception e){
            System.out.println("EXCEPTION"+e.getMessage());
            throw e.getCause();


        }}else{
            return false;
        }

    }
}
