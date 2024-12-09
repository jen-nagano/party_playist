package com.techelevator.dao;

import com.techelevator.model.Song;

import java.util.List;

public interface SongDao {
    Song createSong(Song song);
    List<Song> getSongsByPlaylist(int playlistId);
    void addSongToPlaylist(int playlistId, int songId);
    void removeSongFromPlaylist(int playlistId, int songId);
}
