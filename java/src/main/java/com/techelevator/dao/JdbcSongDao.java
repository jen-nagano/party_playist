package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Song;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcSongDao implements SongDao {

    private static final String UNABLE_TO_CONNECT = "Unable to connect to server or database";
    private static final String DATA_INTEGRITY = "Data integrity violation";
    private final JdbcTemplate jdbcTemplate;

    public JdbcSongDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Song createSong(Song song) {
        Song newSong = null;
        String sql = "INSERT INTO song (title, artist, duration, spotify_id, img_url) VALUES (?, ?, ?, ?, ?) RETURNING *;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, song.getTitle(), song.getArtist(), song.getDuration(), song.getSpotifyId(), song.getImgUrl());
            if (results.next()) {
                newSong = mapRowToSong(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return newSong;
    }

    @Override
    public List<Song> getSongsByPlaylist(int playlistId) {
        List<Song> songs = new ArrayList<Song>();

        String sql = "SELECT s.song_id, s.title, s.artist, s.duration, s.spotify_id, s.img_url " +
                "FROM song s " +
                "JOIN playlist_song ps ON s.song_id = ps.song_id " +
                "WHERE ps.playlist_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, playlistId);
            while (results.next()) {
                songs.add(mapRowToSong(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return songs;
    }

    @Override
    public void addSongToPlaylist(int playlistId, int songId) {
        String sql = "INSERT INTO playlist_song (playlist_id, song_id, position, up_vote, down_vote) " +
                "VALUES (?, ?, 1, 0, 0);";
        try {
            jdbcTemplate.update(sql, playlistId, songId);
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(UNABLE_TO_CONNECT, e);
        }
        catch (DataIntegrityViolationException e) {
            throw new DaoException(DATA_INTEGRITY, e);
        }
    }

    public void removeSongFromPlaylist(int playlistId, int songId) {
        String sql = "DELETE FROM playlist_song WHERE playlist_id = ? AND song_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, playlistId, songId);
            //System.out.println("Rows affected: " + rowsAffected);
            if (rowsAffected == 0) {
                System.out.println("No matching song found in playlist.");
            }
        } catch (Exception e) {
            System.err.println("SQL error: " + e.getMessage());
            throw e;
        }
    }

    private Song mapRowToSong(SqlRowSet results) {
        Song song = new Song();
        song.setSongId(results.getInt("song_id"));
        song.setTitle(results.getString("title"));
        song.setArtist(results.getString("artist"));
        song.setSpotifyId(results.getString("spotify_id"));
        song.setImgUrl(results.getString("img_url"));
        song.setDuration(results.getInt("duration"));
        return song;
    }
}
