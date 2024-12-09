<template>
  <div class="home">

    <!-- Main Content -->
    <div class="content">
      <!-- Welcome Message -->
      <!-- <p class="welcome-message">Welcome to the Panda Party!</p> -->
     
      <!-- Create Event Header -->
      <!-- <h2 class="create-event-header">Your Events</h2> -->

      <!-- Tile Section -->
      <h2 class="event-list-header">The events you are hosting:</h2>
      <div v-if="events.length === 0">You are not hosting any events.</div>
      <div class="event-tiles">
        <div v-for="event in events" :key="event.id" class="event-tile">
          <h3 class="event-title">{{ event.name }}</h3>
          <p class="event-description">{{ event.description }}</p>
          <button class="btn-view-details" @click="viewEvent(event.id)">View Details</button>
          <button class="btn-view-details" @click="removeEvent(event.id)">Remove Event</button>
        </div>
      </div>

      <!-- Create Event Button (Styled as a Panel with Plus Sign) -->
      <button class="btn-create-event" v-on:click="showEvent = true" v-if="!showEvent">
        <span class="plus-sign">+</span>
      </button>

      <div class="Event" v-if="showEvent">
        <h1>Event</h1>
        <p>Complete the form to add a new event.</p>
        <EventForm @formSubmitted="hideEvent"/>
        <!-- <router-link class="btn btn-submit" :to="{ name: 'playlist' }">Create Playlist</router-link> -->
      </div>

      <h2 class="event-list-header">The events you are invited to:</h2>
      <div v-if="guestEvents.length === 0">You are not attending any events.</div>
      <div class="event-tiles">
        <div v-for="event in guestEvents" :key="event.id" class="event-tile">
          <h3 class="event-title">{{ event.name }}</h3>
          <p class="event-description">{{ event.description }}</p>
          <button class="btn-view-details" @click="viewEvent(event.id)">View Details</button>
        </div>
      </div>

      <h2 class="event-list-header">Saved Playlists:</h2>
      <div v-if="savedPlaylists.length === 0">You do not have any saved playlists.</div>
      <div class="event-tiles">
        <div v-for="playlist in savedPlaylists" :key="playlist.id" class="event-tile">
          <h4>{{ playlist.name }}</h4>
          <p>{{ playlist.description }}</p>
          <button @click="viewPlaylist(playlist.playlistId)">View Playlist</button>
          <!-- <h3 class="event-title">{{ event.name }}</h3>
          <p class="event-description">{{ event.description }}</p>
          <button class="btn-view-details" @click="viewEvent(event.id)">View Details</button>
          <button class="btn-view-details" @click="removeEvent(event.id)">Remove Event</button> -->
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import EventForm from '../components/EventForm.vue';
import axios from 'axios'; // Install axios with npm if you haven't: npm install axios

export default {
  components: {
    EventForm
  },
  data() {
    return {
      showEvent: false, // Controls event form visibility
      events: [], // Stores the events fetched from the database
      guestEvents: [],
      savedPlaylists: []
    };
  },
  created() {
    // Fetch events when the component loads
    this.fetchEvents();
    this.fetchSavedPlaylists();
  },
  methods: {
    hideEvent() {
      this.showEvent = false;
      this.fetchEvents(); // Refresh events after a new one is added
    },
    viewPlaylist(playlistId) { 
      this.$router.push({ name: 'PlaylistView', params: { playlistId: playlistId } });
    },
    async fetchEvents() {
      try {
        const userId = this.$store.state.user?.id; // Ensure user.id exists
        if (!userId) {
          console.error('User ID is not defined');
          return;
        }

        const response = await axios.get(`/users/${userId}/events?role=host`);
        this.events = response.data;
      } catch (error) {
        console.error('Error fetching events:', error);
        this.events = []; // Optionally clear the events list on failure
      }
    },
    async fetchGuestEvents() {
      try {
        const userId = this.$store.state.user?.id; // Ensure user.id exists
        if (!userId) {
          console.error('User ID is not defined');
          return;
        }

        const response = await axios.get(`/users/${userId}/events?role=guest`);
        this.guestEvents = response.data;
      } catch (error) {
        console.error('Error fetching events:', error);
        this.guestEvents = []; // Optionally clear the events list on failure
      }
    },
    async fetchSavedPlaylists() {
      try {
        const userId = this.$store.state.user?.id; // Make sure the user ID is available
        if (!userId) {
          console.warn("User ID is not defined.");
          return;
        }

        const response = await axios.get(`/users/${userId}/playlists`);
        this.savedPlaylists = response.data; // Update the component's data property
      } catch (error) {
        if (error.response && error.response.status === 404) {
          console.warn("No saved playlists found for this user.");
        } else {
          console.error("Error fetching saved playlists:", error);
        }
      }
    },
    viewEvent(eventId) {
      console.log('View event details for ID:', eventId);
      this.$router.push({ name: "EventView", params: { eventId } });
      // Add navigation or modal logic here if needed
    }
  }

  
};
</script>


<style scoped>
/* Importing a fun font from Google Fonts */
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap');


/* Body Layout */
.home {
  font-family: 'Poppins', sans-serif;
  margin-top: 60px;  /* Adjusted to avoid overlap with navbar */
}


/* Banner Section */
.banner {
  width: 100%;
  height: 350px;   /* Increase the height of the banner to give more space */
  background-image: url('/src/img/home.png'); /* Your banner image path */
  background-size: cover;
  background-position: top center;  /* This keeps the top part of the image visible */
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  color: white;
  padding-top: 60px; /* Adjust to avoid overlap with navbar, can be fine-tuned */
}


.banner-title {
  font-size: 3rem;
  font-weight: bold;
  text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.5); /* Optional text shadow */
  color: #8e44ad; /* Purple color */
  animation: pulseGlow 1.5s ease-in-out infinite;
}


/* Main Content Styling */
.content {
  text-align: center;
  padding: 20px;
}


/* Welcome Message Styling */
.welcome-message {
  font-size: 3rem; /* Larger font size */
  font-weight: bold; /* Bold text */
  color: #8e44ad; /* Purple color */
  margin-top: 20px;
  text-transform: uppercase;
  letter-spacing: 1px;
  animation: pulseGlow 1.5s ease-in-out infinite;
}


/* Header for Create Event */
.create-event-header {
  font-size: 2.5rem;
  color: #8e44ad; /* Purple color */
  margin-top: 30px;
  font-weight: bold;
}


/* Button Styling for "Create Event" (Styled as a Panel with Plus Sign) */
.btn-create-event {
  display: inline-block;
  background-color: #9b59b6;  /* Purple background color */
  color: white;
  padding: 60px 80px;  /* Increased padding to make the button 2x bigger */
  border-radius: 10px;
  text-decoration: none;
  font-size: 3rem;  /* Increased font size for larger button */
  text-align: center;
  margin-top: 30px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  position: relative;
  animation: glowEffect 1.5s ease-in-out infinite;
}


.btn-create-event:hover {
  background-color: #8e44ad;  /* Darker purple on hover */
  transform: scale(1.1);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}


.plus-sign {
  font-size: 5rem;  /* Increased plus sign size for bigger button */
  margin-right: 10px;
  font-weight: bold;
  animation: bouncePlus 1s infinite;
}


/* Mobile Adjustments */
@media (max-width: 768px) {
  .banner {
    height: 250px; /* Adjust the height for mobile to ensure the top of the image is visible */
  }


  .banner-title {
    font-size: 2rem; /* Smaller title for mobile */
  }


  .welcome-message {
    font-size: 2rem; /* Smaller welcome text on mobile */
  }


  .create-event-header {
    font-size: 2rem; /* Smaller header on mobile */
  }


  .btn-create-event {
    font-size: 2rem; /* Smaller button text for mobile */
    padding: 40px 60px; /* Adjusted padding */
  }


  .plus-sign {
    font-size: 4rem; /* Smaller plus sign for mobile */
  }
}


/* Glowing Animation for Text */
@keyframes pulseGlow {
  0% {
    text-shadow: 0 0 10px #9b59b6, 0 0 20px #9b59b6, 0 0 30px #9b59b6;
  }
  50% {
    text-shadow: 0 0 20px #9b59b6, 0 0 30px #9b59b6, 0 0 40px #9b59b6;
  }
  100% {
    text-shadow: 0 0 10px #9b59b6, 0 0 20px #9b59b6, 0 0 30px #9b59b6;
  }
}


/* Glowing Effect for Button */
@keyframes glowEffect {
  0% {
    box-shadow: 0 0 10px #9b59b6, 0 0 20px #9b59b6, 0 0 30px #9b59b6;
  }
  50% {
    box-shadow: 0 0 20px #9b59b6, 0 0 40px #9b59b6, 0 0 50px #9b59b6;
  }
  100% {
    box-shadow: 0 0 10px #9b59b6, 0 0 20px #9b59b6, 0 0 30px #9b59b6;
  }
}


/* Bouncing animation for the plus sign */
@keyframes bouncePlus {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}
/* added styles here -Jen */
.event-tiles {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 30px;
  justify-content: center;
}

.event-tile {
  background-color: #9b59b6; /* Purple background color */
  color: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  width: 200px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.event-tile:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.event-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.event-description {
  font-size: 1rem;
  margin-bottom: 15px;
}

.btn-view-details {
  background-color: white;
  color: #9b59b6;
  border: 2px solid #9b59b6;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.btn-view-details:hover {
  background-color: #8e44ad;
  color: white;
}
</style>




