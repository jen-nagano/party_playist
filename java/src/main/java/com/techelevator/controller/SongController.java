package com.techelevator.controller;

import com.techelevator.dao.SongDao;
import com.techelevator.model.Song;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SongController {

    private SongDao songDao;

    public SongController(SongDao songDao) {
        this.songDao = songDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/songs", method = RequestMethod.POST)
    public Song addSong(@RequestBody @Valid Song song) {
        // Any logic necessary for creating songs goes here
        return songDao.createSong(song);
    }

    @PostMapping("/test-song")
    public Song testSongParsing(@RequestBody Song song) {
        System.out.println("Parsed song: " + song);
        return song;
    }
}
