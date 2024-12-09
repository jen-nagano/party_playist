<!--
<template>
  <div class="playlist">
    <h1>Playlist</h1>
    <p>Search for a song.</p>
    <SearchSong />
    
  </div>
</template>

<script>
import SearchSong from '../components/SearchSong.vue';

export default {
  name: 'PlaylistView',
  components:{
    SearchSong
  }
};
</script> 
-->

<template>
  <div class="playlist-container">
    <h1>{{ playlist.name }}</h1>

  </div>
  <div class="songs-list mt-6">
      <h2 class="text-2xl font-semibold mb-4">Current Playlist Songs:</h2>
      <div
        v-for="song in playlistSongs"
        :key="song.songId"
        class="song-card"
      >
        <img :src="song.imgUrl" alt="Album Art" />
        <div class="song-info">
          <p class="song-title">{{ song.title }}</p>
          <p class="song-artist">{{ song.artist }}</p>
          <p class="song-duration">{{ this.getDuration(song.duration) }}</p>
        </div>
        <button @click="removeSong(song.songId)">Remove Song</button>
        <!-- <button @click="playSong(song.uri)">Play Song</button> -->
        
      </div>
    </div>
  <div class="playlist-container">
    <h1 class="text-center text-4xl font-bold py-4">Search for songs to add to your playlist.</h1>
    <div class="search-section p-4 bg-gray-800 rounded-lg">
      <!-- <h2 class="text-2xl text-white mb-2">Search for a song and play it!</h2> -->
      <div class="flex items-center space-x-2">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search"
          class="flex-grow px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
        <button
          @click="searchSongs"
          class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-all"
        >
          Search
        </button>
      </div>
    </div>
    <!-- Playback State Section -->
    <!-- <div class="playback-state mt-6">
      <h2 class="text-2xl font-semibold mb-4">Playback State</h2>
      <button
        @click="getPlaybackState"
        class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-all"
      >
        Refresh Playback State
      </button>
      <div v-if="playbackState" class="playback-info mt-4">
        <p><strong>Device:</strong> {{ playbackState.device.name }} ({{ playbackState.device.type }})</p>
        <p><strong>Volume:</strong> {{ playbackState.device.volume_percent }}%</p>
        <p><strong>Now Playing:</strong> {{ playbackState.item.name }} by {{ playbackState.item.artists[0].name }}</p>
        <p><strong>Progress:</strong> {{ formatTime(playbackState.progress_ms) }} / {{ formatTime(playbackState.item.duration_ms) }}</p>
        <p><strong>Playing:</strong> {{ playbackState.is_playing ? "Yes" : "No" }}</p>
      </div>
    </div> -->
    <!-- Songs List Section -->
    <div class="songs-list mt-6">
      <!-- <h2 class="text-2xl font-semibold mb-4">Search for a song title or artist, then add songs to your playlist.</h2> -->
      <div
        v-for="song in songs"
        :key="song.id"
        class="song-card"
      >
        <img :src="song.albumArt" alt="Album Art" />
        <div class="song-info">
          <p class="song-title">{{ song.title }}</p>
          <p class="song-artist">{{ song.artist }}</p>
        </div>
        <button @click="addSong(song)">Add to Playlist</button>
        <!-- <button @click="playSong(song.uri)">Play Song</button> -->
        
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import EventService from "../services/EventService";

export default {
  props: {
    playlistId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      playlist: {},
      searchQuery: "", // User's search input
      songs: [], // Search results from Spotify
      playlistSongs: [], // songs in playlist
      playbackState: null, // Playback state
      spotifyAccessToken: null, // Spotify API token
    };
  },
  created() {
    this.fetchPlaylistDetails();
    this.fetchSongs();
  },
  methods: {
  // Fetch details of the playlist
  async fetchPlaylistDetails() {
    try {
      //const playlistId = this.$route.params.playlistId;
      const response = await axios.get(`/playlists/${this.playlistId}`);
      this.playlist = response.data;
    } catch (error) {
      console.error("Error fetching playlist details:", error);
      alert("Failed to load playlist details.");
    }
  },

  // Fetch playlists for the event
  async fetchSongs() {
    try {
      //const playlistId = this.$route.params.playlistId;
      const response = await axios.get(`/playlists/${this.playlistId}/songs`);
      this.playlistSongs = response.data;
    } catch (error) {
      if (error.response && error.response.status === 404) {
        console.warn("No songs found for this playlist.");
      } else {
        console.error("Error fetching songs:", error);
      }
    }
  },
    async fetchSpotifyToken() {
      try {
        const response = await axios.get("http://localhost:9000/spotify/token");
        this.spotifyAccessToken = response.data.access_token;
        const expiresIn = response.data.expires_in * 1000;
        localStorage.setItem("spotify_access_token", this.spotifyAccessToken);
        localStorage.setItem("spotify_token_expiry", Date.now() + expiresIn);
        console.log("Spotify token fetched:", this.spotifyAccessToken);
      } catch (error) {
        console.error("Error fetching Spotify token:", error);
      }
    },
    async getSpotifyToken() {
      const tokenExpiry = localStorage.getItem("spotify_token_expiry");
      if (!tokenExpiry || Date.now() > tokenExpiry) {
        console.log("Token expired or missing. Fetching a new one...");
        await this.fetchSpotifyToken();
      } else {
        this.spotifyAccessToken = localStorage.getItem("spotify_access_token");
        console.log("Using existing token:", this.spotifyAccessToken);
      }
    },
    async searchSongs() {
      await this.getSpotifyToken();
      //console.log(this.spotifyAccessToken);
      if (!this.spotifyAccessToken) {
        console.error("Spotify token not available.");
        return;
      }
      try {
        const response = await axios.get("https://api.spotify.com/v1/search", {
          params: {
            q: this.searchQuery,
            type: "track",
            limit: 10,
          },
          headers: {
            Authorization: `Bearer ${this.spotifyAccessToken}`,
          },
        });
        this.songs = response.data.tracks.items.map((track) => ({
          id: track.id,
          title: track.name,
          artist: track.artists.map((artist) => artist.name).join(", "),
          albumArt: track.album.images[0]?.url,
          uri: track.uri,
          duration: track.duration_ms
        }));
        console.log("Search results:", this.songs);
      } catch (error) {
        console.error("Error searching Spotify:", error);
      }
    },
    async playSong(songUri) {
      await this.getSpotifyToken();
      if (!this.spotifyAccessToken) {
        console.error("Spotify token not available.");
        return;
      }
      try {
        // Get available devices
        const devicesResponse = await axios.get("https://api.spotify.com/v1/me/player/devices", {
          headers: {
            Authorization: `Bearer ${this.spotifyAccessToken}`,
          },
        });
        const devices = devicesResponse.data.devices;
        if (!devices.length) {
          alert("No active Spotify devices found. Open Spotify on a device and try again.");
          return;
        }
        // Start playback on the first available device
        const deviceId = devices[0].id; // Use the first available device
        await axios.put(
          `https://api.spotify.com/v1/me/player/play?device_id=${deviceId}`,
          { uris: [songUri] }, // Play the specified song
          {
            headers: {
              Authorization: `Bearer ${this.spotifyAccessToken}`,
            },
          }
        );
        console.log("Playback started on device:", deviceId);
      } catch (error) {
        console.error("Error playing song:", error.response?.data || error.message);
      }
    },
    addSong(song) {
      console.log(song);
      const plainSong = JSON.parse(JSON.stringify(song));
      console.log(plainSong);
      const mySong = {
        title:song.title,
        artist:song.artist,
        duration:song.duration,
        spotifyId:song.uri.substring(14),
        imgUrl:song.albumArt
      }
      console.log(mySong);
      EventService
        .addSongToPlaylist(mySong, this.playlist.playlistId)
        .then(response => {
          if (response.status === 201) {
            // this.$store.commit(
            //   'SET_NOTIFICATION',
            //   {
            //     message: 'A new event was added.',
            //     type: 'success'
            //   }
            // );
            console.log(response.data);
            this.fetchSongs();
          }
        })
        .catch(error => {
          console.log(error, 'adding');
        });
    },
    async removeSong(songId) {
      console.log("Removing song with ID:", songId);
      try {
        await axios.delete(`/playlists/${this.playlistId}/songs/${songId}`);
        console.log(`Song with ID ${songId} removed from playlist ${this.playlistId}`);
        // Refresh the list of songs in the playlist
        this.fetchSongs();
      } catch (error) {
        console.error("Error removing song from playlist:", error);
        alert("Failed to remove the song from the playlist.");
      }
    },

    getDuration(duration_ms) {
      let duration = Math.round(duration_ms/1000);
      let minutes = Math.round(duration/60);
      let seconds = duration%60;
      return minutes + ":" + seconds;
    },
    async getPlaybackState() {
      await this.getSpotifyToken();
      if (!this.spotifyAccessToken) {
        console.error("Spotify token not available.");
        return;
      }
      try {
        const response = await axios.get("https://api.spotify.com/v1/me/player", {
          headers: {
            Authorization: `Bearer ${this.spotifyAccessToken}`,
          },
        });
        this.playbackState = response.data;
        console.log("Playback state:", this.playbackState);
      } catch (error) {
        console.error("Error fetching playback state:", error);
      }
    },
    formatTime(milliseconds) {
      const minutes = Math.floor(milliseconds / 60000);
      const seconds = Math.floor((milliseconds % 60000) / 1000);
      return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
    },
  },
  mounted() {
    this.getSpotifyToken(); // Fetch the token when the component is mounted
  },
};
</script>
<style scoped>
.playlist-container {
  max-width: 800px;
  margin: 0 auto;
}
.song-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 8px;
  margin-bottom: 8px;
  background-color: #070707;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.song-card img {
  width: 50px;
  height: 50px;
  border-radius: 4px;
}
.song-card .song-info {
  flex: 1;
}
button {
  padding: 4px 8px;
  font-size: 14px;
  background-color: #28A745;
  color: white;
  border: none;
  border-radius: 4px;
}
button:hover {
  background-color: #218838;
}
</style>

