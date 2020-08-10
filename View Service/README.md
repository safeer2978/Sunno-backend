# View Service

This service is responsible for maintaining and providing music data. It consists of a Rest controller that serves the client with information about the music available for streaming. 

This service is required by the client to browse through the music and to get a `track_id` with which the client can make a request to the `play-service`.

## Data Model:

The Track Data consists of the following entities:
	
* Track
* Artist
* Album
* Genre
* Playlist

The relations between each of these entities given in the figure.

![alt text](https://github.com/safeer2978/Sunno-backend/blob/master/Diagrams/view-service-datamodel.png)

Each of the entity consists of an ID, Name and Image Url, along with the corresponding relationship attributes.

In the case of an album, the issue is that album name cannot be a unique entity as several artists may have the same album name, hence to avoid duplication, a derived attribute called `combination` is made combining album name and artist_id. 

Ultimately the schema boils down to :


* `album (id, name, img_url, genre_id, artist_id, combination)`
* `artist (id, name, img_url)`
* `artist_track(id, artist_id, track_id)`
* `genre (id, name, img_url)`
* `playlist (id, name, user_id)`
* `playlist_track (playlist_id, track_id)`
* `track (id, name, img_url)`


You may refer to the view-service-mysql.sql in the SQL folder for the query to create this schema in MySQL.


The application relies on JPA annotations for mapping relationships; and makes use of Hibernate ORM.

## API Endpoints:

* `/metadata` 			- Returns list of all entitites except for Track.
* `/artists` 			- Returns a list of Artists
* `/artist/{artist_id}` - Returns Artist data of specific artist
* `/albums`				- Returns a list of Albums
* `/album/{album_id}`	- Returns Album data of specific album
* `/playlist/{user_id}` - returns playlists of given user. user_id=0 is for admin created playlists
* `/playlist/create`	- Creates playlist for a given user
* `/playlist/add/{playlist_id}/{track_id}`		- Adds track to a playlist.
* `/playlist/remove/{playlist_id}/{track_id}`	- Removes track from playlist.

There's alot of scope for improvement here and will be done in subsequently.

There'll also be a GraphQL endpoint in the future.

## Saving Music Data:

This service has handled a request from upload application, which consists of updated information of music available for streaming as `MusicDataRequest`. 
The service handles this request through the `/updateMusicData` endpoint.

This method calls the `saveMusicData()` method of Service and translates all contents of `MusicData` to fit in the data model w.r.t the relations. It returns a boolean value.

the `/updateMusicData` Takes a list of `MusicData` objects and returns a List of boolean indicating the status of each object. 

You'll find more information on this in the `upload_appliation` directory.

## Usage:

This app makes use of Mysql through Hibernate ORM. you need to specify the Mysql credentials in `application.properties` 

You also need to specify the link to the eureka naming server.
