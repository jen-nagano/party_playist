<template>
  <div class="event-container">
    <h2>{{ event.name }}</h2>
    <p>{{ event.description }}</p>
    <p><strong>Start Time:</strong> {{ event.startTime }}</p>
    <p><strong>End Time:</strong> {{ event.endTime }}</p>
    <p><strong>Date:</strong> {{ event.date }}</p>
    <h3>Playlists</h3>
    <div class="playlist-tiles">
      <div
        v-for="playlist in playlists"
        :key="playlist.playlistId"
        class="playlist-tile"
      >
        <h4>{{ playlist.name }}</h4>
        <p>{{ playlist.description }}</p>
        <button @click="viewPlaylist(playlist.playlistId)" class="btn-view-playlist">View Playlist</button>
        <button @click="savePlaylist(playlist.playlistId)" class="btn-view-playlist">Save Playlist</button>
      </div>
      <p v-if="playlists.length === 0">No playlists found for this event.</p>
      <div>
        <button class="btn-create-playlist" v-on:click="this.showPlaylist=true">
          Create Playlist
        </button>
      </div>
    </div>
    <div class="show_playlist" v-if="showPlaylist">
      <form v-on:submit.prevent="submitForm" class="playlistForm">
        <div class="form-group">
          <label for="playlist-name">Choose a name for your playlist:</label>
          <input id="playlist-name" type="text" class="form-control" v-model="editPlaylist.name" autocomplete="off" />
        </div>
        <button class="btn btn-submit">Create Playlist</button>
        <button class="btn btn-cancel" v-on:click="cancelForm" type="button">Cancel</button>
      </form>
    </div>
    <div class="qr-code-container">
      <button @click="toggleQRCode" class="btn-qr-code">
        Show QR Code
      </button>
      <div v-if="showQRCode" class="qr-code">
        <img :src="qrCode" alt="Event QR Code" class="qr-image" />
        <button @click="downloadQRCode" class="btn-download-qr">Download QR Code</button>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';
import EventService from '../services/EventService';
export default {
  data() {
    return {
      event: {},
      playlists: [],
      showPlaylist: false,
      showQRCode: false,
      qrCode: '',
      editPlaylist: {
        name: '',
        userId: this.$store.state.user.id
      }
    };
  },
  created() {
    this.fetchEventDetails();
    this.fetchPlaylists();
  },
  methods: {
    async fetchEventDetails() {
      try {
        const eventId = this.$route.params.eventId;
        const response = await axios.get(`/events/${eventId}`);
        this.event = response.data;
        this.generateQRCode();
      } catch (error) {
        console.error("Error fetching event details:", error);
        alert("Failed to load event details.");
      }
    },
    async fetchPlaylists() {
      try {
        const eventId = this.$route.params.eventId;
        const response = await axios.get(`/events/${eventId}/playlists`);
        this.playlists = response.data;
      } catch (error) {
        if (error.response && error.response.status === 404) {
          console.warn("No playlists found for this event.");
        } else {
          console.error("Error fetching playlists:", error);
        }
      }
    },
    viewPlaylist(playlistId) {
      this.$router.push({ name: 'PlaylistView', params: { playlistId: playlistId } });
    },
    savePlaylist(playlistId) {
      EventService
        .savePlaylist(this.$store.state.user.id, playlistId)
        .then(response => {
          if (response.status === 201) {
            // this.$store.commit(
            //   'SET_NOTIFICATION',
            //   {
            //     message: 'A new event was added.',
            //     type: 'success'
            //   }
            // );
            //console.log(response.data);
            //this.$router.push({ name: 'PlaylistView', params: { playlistId: response.data.playlistId } });
          }
        })
        .catch(error => {
          console.log(error, 'adding');
        });
    },
    submitForm() {
      if (this.editPlaylist.name === '') {
        return;
      }
      EventService
        .addPlaylistToEvent(this.editPlaylist, this.$route.params.eventId)
        .then(response => {
          if (response.status === 201) {
            console.log(response.data);
            this.$router.push({ name: 'PlaylistView', params: { playlistId: response.data.playlistId } });
          }
        })
        .catch(error => {
          console.log(error, 'adding');
        });
      this.showPlaylist = false;
    },
    cancelForm() {
      this.showPlaylist = false;
    },
    toggleQRCode() {
      this.showQRCode = !this.showQRCode;
    },
    generateQRCode() {
      const eventId = this.$route.params.eventId;
      this.qrCode = `https://api.qrserver.com/v1/create-qr-code/?data=/events/${eventId}&size=150x150`;
    },
    downloadQRCode() {
      const link = document.createElement('a');
      link.href = this.qrCode;
      link.download = `event-${this.event.name}-qr-code.png`;
      link.click();
    }
  }
};
</script>
<style scoped>
.event-container {
  text-align: center;
  font-size: 2rem;
}
.playlist-tiles {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  justify-content: center;
}
.playlist-tile {
  background: linear-gradient(145deg, #8E44AD, #0E0101);
  border: 2px solid #ddd;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.2);
  width: 320px;
  transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}
.playlist-tile:hover {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.4);
  background: linear-gradient(145deg, #F39C12, #C0392B);
}
.playlist-tile h4 {
  margin: 0;
  font-size: 2.5rem;
}
.playlist-tile p {
  font-size: 1.8rem;
  color: #555;
}
.btn-create-playlist {
  background-color: #3498DB;
  color: white;
  padding: 15px 30px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 2rem;
  transition: transform 0.3s ease, background-color 0.3s ease, box-shadow 0.3s ease;
  margin-top: 20px;
}
.btn-create-playlist:hover {
  background-color: #2980B9;
  transform: scale(1.1);
  box-shadow: 0 0 15px #2980B9;
}
.btn-view-playlist {
  background-color: #E74C3C;
  color: white;
  padding: 12px 25px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 2rem;
  transition: transform 0.3s ease, background-color 0.3s ease, box-shadow 0.3s ease;
}
.btn-view-playlist:hover {
  background-color: #C0392B;
  transform: scale(1.1);
  box-shadow: 0 0 15px #C0392B;
}
.btn-qr-code {
  background-color: #F39C12;
  color: white;
  padding: 15px 30px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 2rem;
  transition: background-color 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
}
.btn-qr-code:hover {
  background-color: #E67E22;
  transform: scale(1.1);
  box-shadow: 0 0 20px #E67E22;
}
.qr-code-container {
  margin-top: 20px;
}
.qr-image {
  width: 200px;
  height: 200px;
  margin-bottom: 20px;
}
.btn-download-qr {
  background-color: #2ECC71;
  color: white;
  padding: 12px 24px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 2rem;
  transition: background-color 0.3s ease, transform 0.3s ease;
}
.btn-download-qr:hover {
  background-color: #27AE60;
  transform: scale(1.1);
}
</style>