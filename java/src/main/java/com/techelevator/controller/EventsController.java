package com.techelevator.controller;

import com.techelevator.dao.EventDao;
import com.techelevator.dao.PlaylistDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Event;
import com.techelevator.model.Playlist;
import com.techelevator.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
// test for push
@RestController
@CrossOrigin
public class EventsController {

    private EventDao eventDao;
    private PlaylistDao playlistDao;
    private UserDao userDao;

    public EventsController(EventDao eventDao, PlaylistDao playlistDao, UserDao userDao){
        this.eventDao = eventDao;
        this.playlistDao = playlistDao;
        this.userDao = userDao;
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        // Step 1: Validate the creator (user_id)
        User user = userDao.getUserById(event.getCreator());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator not found");
        }

        // Step 2: Create the event
        Event newEvent = eventDao.createEvent(event);

        // Step 3: Add the creator as a "host" in the user_event table
        eventDao.addUserEventRelationship(event.getCreator(), newEvent.getId(), "host");

        return newEvent;
    }

    @RequestMapping(path="/events/{eventId}", method=RequestMethod.GET)
    public Event getEventById(@PathVariable int eventId) {
        Event event = eventDao.getEventById(eventId);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        return event;
    }

    @RequestMapping(path="/events/{eventId}/playlists", method=RequestMethod.GET)
    public List<Playlist> getPlaylistsForEvent(@PathVariable int eventId) {
        // Step 1: Ensure the event exists
        Event event = eventDao.getEventById(eventId); // Assuming `getEventById` exists in EventDao
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

        // Step 2: Retrieve playlists associated with the event
        List<Playlist> playlists = playlistDao.getPlaylistsByEventId(eventId);
        if (playlists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No playlists found for this event");
        }

        return playlists;
    }

    @RequestMapping(path = "/events/{eventId}/playlists", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Playlist addPlaylistToEvent(@PathVariable int eventId, @RequestBody Playlist playlist) {
        // Step 1: Ensure the event exists
        Event event = eventDao.getEventById(eventId);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

        // Step 2: Validate the playlist data (if necessary)
        if (playlist.getName() == null || playlist.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Playlist name is required");
        }

        // Step 3: Create the new playlist (if it's not already in the system)
        Playlist newPlaylist = playlistDao.createPlaylist(playlist);

        // Step 4: Link the playlist to the event
        playlistDao.linkPlaylistToEvent(eventId, newPlaylist.getPlaylistId());

        return newPlaylist;
    }

    @GetMapping("/users/{userId}/events")
    public List<Event> getUserEvents(@PathVariable int userId, @RequestParam String role) {
        // Validate user existence
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Retrieve events where the user has the specified role
        List<Event> events = eventDao.getEventsByUserAndRole(userId, role);
        if (events.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No events found for user in role: " + role);
        }

        return events;
    }

    @PostMapping("/test-events")
    public Event testEventParsing(@RequestBody Event event) {
        System.out.println("Parsed event: " + event);
        return event;
    }

}
