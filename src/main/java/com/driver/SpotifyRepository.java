package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user=new User(name,mobile);
        users.add(user);
        return user;
    }



    public Artist createArtist(String name) {
        Artist artist=new Artist(name);
        artists.add(artist);
        return artist;
    }



    public Album createAlbum(String title, String artistName) {
        Artist artist=new Artist();
        boolean present=false;
        for(Artist artistss:artists){
            if(artistss.getName().compareTo(artistName)==0){
                artist=artistss;
                present=true;
                break;
            }
        }
        if(!present){
            artist=new Artist(artistName);
            artists.add(artist);
        }
        Album album=new Album(title);
        albums.add(album);
        if(artistAlbumMap.containsKey(artist)){
            List<Album> oldlist=artistAlbumMap.get(artist);
            oldlist.add(album);
            artistAlbumMap.put(artist,oldlist);
        }
        else{
            List<Album> newlist=new ArrayList<>();
            newlist.add(album);
            artistAlbumMap.put(artist,newlist);
        }
        return album;
    }




    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song=new Song(title,length);
        songs.add(song);
        boolean present=false;
        for(Album album:albumSongMap.keySet()){
            if(album.getTitle().compareTo(albumName)==0){
                present=true;
                List<Song> oldlist=new ArrayList<>();
                oldlist.add(song);
                albumSongMap.put(album,oldlist);
                return song;
            }
        }
        throw new RuntimeException("Album does not exist");
    }





    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
            User user=new User();
            boolean userfound=false;
            for(User user1:users){
                if(user1.getMobile().compareTo(mobile)==0){
                    user=user1;
                    userfound=true;
                    break;
                }
            }
            if(!userfound) throw new RuntimeException("User does not exist");
              Playlist playlist=new Playlist(title);
              playlists.add(playlist);
              List<Song> newList=new ArrayList<>();
              for (Song song : songs){
                  if(song.getLength()==length){
                      newList.add(song);
                  }
              }
              playlistSongMap.put(playlist,newList);
              creatorPlaylistMap.put(user,playlist);
              if(userPlaylistMap.containsKey(user)){
                  List<Playlist> oldlist_playlist=userPlaylistMap.get(user);
                  oldlist_playlist.add(playlist);
                  userPlaylistMap.put(user,oldlist_playlist);
              }
              else{
                  List<Playlist> newlist_playlist=new ArrayList<>();
                  newlist_playlist.add(playlist);
                  userPlaylistMap.put(user,newlist_playlist);
              }
              List<User> playListUser=new ArrayList<>();
              playListUser.add(user);
              playlistListenerMap.put(playlist,playListUser);
              return playlist;
    }





    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user=new User();
        boolean userfound=false;
        for(User user1:users){
            if(user1.getMobile().compareTo(mobile)==0){
                user=user1;
                userfound=true;
                break;
            }
        }
        if(!userfound) throw new RuntimeException("User does not exist");
        Playlist playlist=new Playlist(title);
        playlists.add(playlist);
        List<Song> newList=new ArrayList<>();
        HashMap<String,Song> hmsong=new HashMap<>();
        for (Song song : songs){
            hmsong.put(song.getTitle(),song);
        }
        for (String name:songTitles){
            if(hmsong.containsKey(name)){
                newList.add(hmsong.get(name));
            }
        }
        playlistSongMap.put(playlist,newList);
        creatorPlaylistMap.put(user,playlist);
        if(userPlaylistMap.containsKey(user)){
            List<Playlist> oldlist_playlist=userPlaylistMap.get(user);
            oldlist_playlist.add(playlist);
            userPlaylistMap.put(user,oldlist_playlist);
        }
        else{
            List<Playlist> newlist_playlist=new ArrayList<>();
            newlist_playlist.add(playlist);
            userPlaylistMap.put(user,newlist_playlist);
        }
        List<User> playListUser=new ArrayList<>();
        playListUser.add(user);
        playlistListenerMap.put(playlist,playListUser);
        return playlist;
    }




    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        User user=new User();
        boolean userfound=false;
        for(User user1:users){
            if(user1.getMobile().compareTo(mobile)==0){
                user=user1;
                userfound=true;
                break;
            }
        }
        if(!userfound) throw new RuntimeException("User does not exist");
        Playlist pl=new Playlist();
        boolean playListFound=false;
        for(Playlist playlist:playlists){
            if(playlist.getTitle().compareTo(playlistTitle)==0){
                pl=playlist;
                playListFound=true;
                break;
            }
        }
        if(!playListFound) throw new RuntimeException("Playlist does not exist");
            List<User> userdata=playlistListenerMap.get(pl);
            boolean present=false;
            for(User user1:userdata){
                if(user1.equals(user)){
                    present=true;
                    break;
                }
            }
            if(!present) userdata.add(user);
            playlistListenerMap.put(pl,userdata);

        return pl;
    }




    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user=new User();
        boolean userfound=false;
        for(User user1:users){
            if(user1.getMobile().compareTo(mobile)==0){
                user=user1;
                userfound=true;
                break;
            }
        }
        if(!userfound) throw new RuntimeException("User does not exist");
        Song song=new Song();
        boolean songFound=false;
        for (Song song1:songs){
            if(song1.getTitle().compareTo(songTitle)==0){
                song=song1;
                songFound=true;
                break;
            }
        }
        if(!songFound) throw new RuntimeException("Song does not exist");
        if(songLikeMap.containsKey(song)){
            List<User> userList=songLikeMap.get(song);
            for(User user1:userList){
                if (user1.equals(user)) return song;
            }
            userList.add(user);
            songLikeMap.put(song,userList);
        }
        else{
            List<User> userList=new ArrayList<>();
            userList.add(user);
            songLikeMap.put(song,userList);
        }
        song.setLikes(song.getLikes()+1);
        Album album=new Album();
        for(Album album1:albumSongMap.keySet()){
            List<Song> allsongs=albumSongMap.get(album1);
            boolean albumpresent=false;
            for (Song song1:allsongs){
                if (song1.equals(song)){
                    album=album1;
                    albumpresent=true;
                    break;
                }
            }
            if(albumpresent) break;
        }

        Artist artist=new Artist();
        for(Artist artist1:artistAlbumMap.keySet()){
            List<Album> albumList=artistAlbumMap.get(artist1);
            boolean artistFound=false;
            for (Album album1:albumList){
                if (album1.equals(album)){
                    artistFound=true;
                    artist=artist1;
                    break;
                }
            }
            if(artistFound) break;
        }
        artist.setLikes(artist.getLikes()+1);
        return song;
    }




    public String mostPopularArtist() {
        String mostLiked=null;
        int max=0;
        for(Artist artist:artists){
            if(artist.getLikes()>=max){
                max=artist.getLikes();
                mostLiked=artist.getName();
            }
        }
        if(mostLiked==null) return null;
        return mostLiked;
    }



    public String mostPopularSong() {
        String mostLiked=null;
        int max=0;
        for(Song song: songs){
            if(song.getLikes()>=max){
                max=song.getLikes();
                mostLiked=song.getTitle();
            }
        }
        if(mostLiked==null) return null;
        return mostLiked;
    }
}
