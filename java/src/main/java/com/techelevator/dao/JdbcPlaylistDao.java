package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Event;
import com.techelevator.model.Playlist;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPlaylistDao implements PlaylistDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcPlaylistDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) {
        Playlist newPlaylist = null;
        String sql = "INSERT INTO playlist (name, user_id) VALUES (?, ?);";

        try {
            // Use update() since INSERT doesn't return rows
            jdbcTemplate.update(sql, playlist.getName(), playlist.getUserId());

            // After insertion, you may want to retrieve the new playlist details
            // For example, to fetch the playlist's ID (if it was auto-generated)
            String selectSql = "SELECT playlist_id, name, user_id FROM playlist WHERE name = ? AND user_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(selectSql, playlist.getName(), playlist.getUserId());
            if (results.next()) {
                newPlaylist = mapRowToPlaylist(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return newPlaylist;
    }

    @Override
    public Playlist getPlaylistById(int playlistId) {
        Playlist playlist = null;
        String sql = "SELECT playlist_id, name, user_id FROM playlist WHERE playlist_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, playlistId);
            if (results.next()) {
                playlist = mapRowToPlaylist(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("An unexpected error occurred while retrieving the playlist", e);
        }

        return playlist;
    }

    @Override
    public List<Playlist> getPlaylistsByEventId(int eventId) {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT p.playlist_id, p.name, p.user_id " +
                "FROM playlist p " +
                "JOIN event_playlist ep ON p.playlist_id = ep.playlist_id " +
                "WHERE ep.event_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, eventId);
            while (results.next()) {
                playlists.add(mapRowToPlaylist(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return playlists;
    }

    @Override
    public void linkPlaylistToEvent(int eventId, int playlistId) {
        String sql = "INSERT INTO event_playlist (playlist_id, event_id) VALUES (?, ?);";
        try {
            jdbcTemplate.update(sql, playlistId, eventId);
        } catch (Exception e) {
            throw new DaoException("Error linking playlist to event", e);
        }
    }

    // Save a playlist for a user in the user_playlist table
    public void savePlaylistForUser(int userId, int playlistId) {
        String sql = "INSERT INTO user_playlist (user_id, playlist_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, playlistId);
    }

    /**
     * Fetch all playlists for a given user using the user_playlist join table
     * @param userId the ID of the user
     * @return List of playlists associated with the user
     */
    public List<Playlist> getPlaylistsByUserId(int userId) {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT p.* " +
                "FROM playlist p " +
                "JOIN user_playlist up ON p.playlist_id = up.playlist_id " +
                "WHERE up.user_id = ?;";
        System.out.println(sql);

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                System.out.print("getting a playlist for user: " + userId);
                playlists.add(mapRowToPlaylist(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return playlists;
    }

    private Playlist mapRowToPlaylist(SqlRowSet results) {
        Playlist playlist = new Playlist();

        playlist.setPlaylistId(results.getInt("playlist_id"));
        playlist.setName(results.getString("name"));
        playlist.setUserId(results.getInt("user_id"));

        return playlist;
    }
}
