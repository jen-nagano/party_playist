<template>
    <div class="home">
      <h1>Add a song to this playlist</h1>
      <p>Put playlist title here later</p>
     
      <!-- Form for searching tracks by name -->
      <form v-on:submit.prevent="searchTrack()" class="cardForm">
        <div class="form-group">
          <label for="track-name">Search:</label>
          <input id="track-name" type="text" class="form-control" v-model="trackName" autocomplete="off" /><br/>
        </div>
        <br/>
        <button class="btn btn-submit">Search</button>
      </form>
      <!-- Display search results -->
      <ul v-if="searchResults.length">
        <li v-for="track in searchResults" :key="track.id">
            <img :src="track.album.images[0]?.url" alt="Album cover" width="50" />
            {{ track.name }} by {{ track.artists[0].name }} <br/>
            Duration: {{ displayDuration(track.duration_ms) }}
            <button>Add to Playlist</button>
        </li>
      </ul>
    </div>
  </template>
  <script>
  import SpotifyService from '../services/SpotifyService.js';
  export default {
    data() {
      return {
        trackName: '', // To bind the input field
        searchResults: [] // To store search results
      };
    },
    methods: {
      searchTrack() {
        SpotifyService.searchTrack(this.trackName)
          .then(response => {
            this.searchResults = response.data.tracks.items; // Store search results
            console.log(this.searchResults);
          })
          .catch(error => {
            console.error('Error searching track:', error);
          });
      },
      displayDuration(duration) {
            let totalSeconds = Math.round(duration/1000);
            console.log(totalSeconds);
            let minutes = Math.floor(totalSeconds / 60);
            let seconds = totalSeconds - minutes * 60;
            let displayTime = minutes + ":" + seconds;
            return displayTime;
        }
    }
  };
  </script>
  <style scoped>
  .home {
    padding: 20px;
  }
  .cardForm {
    margin-bottom: 20px;
  }
  </style>