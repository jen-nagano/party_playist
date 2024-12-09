package com.techelevator.dao;

import com.techelevator.model.Event;

import java.util.List;

public interface EventDao {
    Event createEvent(Event event);
    Event getEventById(int eventId);
    List<Event> getEventsByUserAndRole(int userId, String role);
    void addUserEventRelationship(int userId, int eventId, String role);

}
