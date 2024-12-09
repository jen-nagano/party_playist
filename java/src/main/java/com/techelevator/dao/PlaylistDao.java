package com.techelevator.dao;

import com.techelevator.model.Playlist;

import java.util.List;

public interface PlaylistDao {

    Playlist createPlaylist(Playlist playlist);
    Playlist getPlaylistById(int playlistId);
    List<Playlist> getPlaylistsByEventId(int eventId);
    void linkPlaylistToEvent(int eventId, int playlistId);
    void savePlaylistForUser(int userId, int playlistId);
    List<Playlist> getPlaylistsByUserId(int userId);
}
