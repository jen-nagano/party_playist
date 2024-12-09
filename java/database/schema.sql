BEGIN TRANSACTION;

DROP TABLE IF EXISTS user_event;
DROP TABLE IF EXISTS playlist_song;
DROP TABLE IF EXISTS event_playlist;
DROP TABLE IF EXISTS user_playlist;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS song;
DROP TABLE IF EXISTS playlist;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE event (
	event_id SERIAL,
	name TEXT NOT NULL,
	date DATE NOT NULL,
	start_time TEXT,
	end_time TEXT,
	creator INTEGER,
	CONSTRAINT PK_event PRIMARY KEY (event_id),
	CONSTRAINT FK_creator FOREIGN KEY (creator) REFERENCES users(user_id)
);

CREATE TABLE playlist (
    playlist_id SERIAL,
    name TEXT,
    user_id INTEGER,
    CONSTRAINT PK_playlist PRIMARY KEY (playlist_id),
    CONSTRAINT FK_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE song (
    song_id SERIAL,
    title TEXT,
    artist TEXT,
    duration INTEGER,
    spotify_id VARCHAR(30),
    img_url TEXT,
    CONSTRAINT PK_song PRIMARY KEY (song_id)
);

CREATE TABLE playlist_song (
    ps_id SERIAL,
    playlist_id INTEGER,
    song_id INTEGER,
    position INTEGER,
    up_vote INTEGER,
    down_vote INTEGER,
    CONSTRAINT PK_playlist_song PRIMARY KEY (ps_id),
    CONSTRAINT FK_playlist_id FOREIGN KEY (playlist_id) REFERENCES playlist(playlist_id),
    CONSTRAINT FK_song_id FOREIGN KEY (song_id) REFERENCES song(song_id)
);

CREATE TABLE event_playlist (
    ep_id SERIAL,
    playlist_id INTEGER,
    event_id INTEGER,
    CONSTRAINT PK_event_playlist PRIMARY KEY (ep_id),
    CONSTRAINT FK_event_playlist_id FOREIGN KEY (playlist_id) REFERENCES playlist(playlist_id),
    CONSTRAINT FK_playlist_event_id FOREIGN KEY (event_id) REFERENCES event(event_id)
);

CREATE TABLE user_event (
    ue_id SERIAL,
    user_id INTEGER,
    event_id INTEGER,
    role VARCHAR(20),
    CONSTRAINT PK_user_event PRIMARY KEY (ue_id),
    CONSTRAINT FK_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_host_event_id FOREIGN KEY (event_id) REFERENCES event(event_id)
);

CREATE TABLE user_playlist (
    up_id SERIAL,
    user_id INTEGER,
    playlist_id INTEGER,
    CONSTRAINT PK_user_playlist PRIMARY KEY (up_id),
    CONSTRAINT FK_user_id_with_playlist FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_playlist_id_with_user FOREIGN KEY (playlist_id) REFERENCES playlist(playlist_id)
);

COMMIT TRANSACTION;
