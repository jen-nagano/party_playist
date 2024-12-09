package com.techelevator.controller;

import com.techelevator.dao.PlaylistDao;
import com.techelevator.dao.SongDao;
import com.techelevator.model.Playlist;
import com.techelevator.model.Song;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
public class PlaylistController {

    private PlaylistDao playlistDao;
    private SongDao songDao;

    public PlaylistController(PlaylistDao playlistDao, SongDao songDao) {
        this.playlistDao = playlistDao;
        this.songDao = songDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/playlists", method = RequestMethod.POST)
    public Playlist addPlaylist(@RequestBody @Valid Playlist playlist) {
        // Any logic necessary for creating playlists goes here
        return playlistDao.createPlaylist(playlist);
    }

    @RequestMapping(path="/playlists/{playlistId}", method=RequestMethod.GET)
    public Playlist getPlaylistById(@PathVariable int playlistId) {
        Playlist playlist = playlistDao.getPlaylistById(playlistId);
        if (playlist == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found");
        }
        return playlist;
    }

    @PostMapping("/test-playlist")
    public Playlist testPlaylistParsing(@RequestBody Playlist playlist) {
        System.out.println("Parsed playlist: " + playlist);
        return playlist;
    }

    @RequestMapping(path = "/playlists/{id}/songs", method = RequestMethod.GET)
    public List<Song> getSongsByPlaylist(@PathVariable("id") int playlistId) {
        List<Song> songs = songDao.getSongsByPlaylist(playlistId);
        if (songs == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found.");
        } else {
            return songs;
        }
    }

    @RequestMapping(path = "/playlists/{playlistId}/songs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addSongToPlaylist(@PathVariable int playlistId, @RequestBody Song song) {
        // Step 1: Create the new song
        Song newSong;
        try {
            newSong = songDao.createSong(song);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create song", e);
        }

        // Step 2: Link the song to the playlist
        try {
            songDao.addSongToPlaylist(playlistId, newSong.getSongId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to link song to playlist", e);
        }
    }

    @RequestMapping(path = "/playlists/{playlistId}/songs/{songId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSongFromPlaylist(@PathVariable int playlistId, @PathVariable int songId) {
        try {
            songDao.removeSongFromPlaylist(playlistId, songId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to remove song from playlist", e);
        }
    }

    @RequestMapping(path = "/users/{userId}/playlists/{playlistId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void savePlaylistForUser(@PathVariable int userId, @PathVariable int playlistId) {
        try {
            playlistDao.savePlaylistForUser(userId, playlistId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save playlist for user", e);
        }
    }

    @RequestMapping(path = "/users/{userId}/playlists", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Playlist> getPlaylistsForUser(@PathVariable int userId) {
        try {
            return playlistDao.getPlaylistsByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch playlists for user", e);
        }
    }
}