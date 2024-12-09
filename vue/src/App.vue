<template>
  <div id="capstone-app">
    <div id="nav">
      <div class="nav-container">
        <!-- Image next to the text -->
        <div class="logo-container">
          <img src="/src/img/nav.png" alt="Logo" class="logo-img">
          <router-link v-bind:to="{ name: 'home' }" class="nav-link logo">Panda Party Playlist</router-link>
        </div>
        <div class="nav-links">
          <router-link v-bind:to="{ name: 'home' }" class="nav-link">Home</router-link>
         
          <!-- Only show this if the user is authenticated -->
          <li v-if="isAuthenticated">
            <span class="user-indicator">Welcome, {{ username }}</span>
          </li>
         
          <!-- Logout link, only visible if user is authenticated -->
          <router-link v-if="isAuthenticated" v-bind:to="{ name: 'logout' }" class="nav-link">Logout</router-link>
        </div>
      </div>
    </div>
    <router-view />
  </div>
</template>


<script>
export default {
  computed: {
    isAuthenticated() {
      return this.$store.state.token !== ''; // Check if token exists
    },
    username() {
      return this.$store.state.user?.username || 'Guest'; // Display username or default to 'Guest'
    },
  },


  mounted() {
    // Check the authentication status when the component mounts
    this.checkAuthentication();
  },
  methods: {
    // This method will check if the user is authenticated and set the username
   
   
    logout() {
      // Handle logout logic here (clear token, username, etc.)
      this.$store.commit('clearAuthData'); // Example, implement according to your Vuex setup
      this.isAuthenticated = false;
      this.username = "";
      this.$router.push('/login'); // Redirect to login page
    }
  }
};
</script>


<style scoped>
/* Global Dark Theme */
#capstone-app {
  font-family: 'Arial', sans-serif;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(to right, #070707, #fcfafb); /* Vibrant pink gradient */
  color: #ecf0f1;
}


#nav {
  background-color: #741577;
  padding: 20px 30px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.5);
  border-radius: 15px; /* Rounded corners for a more stylish look */
  transition: background-color 0.3s ease-in-out, transform 0.3s ease-in-out;
  position: sticky;
  top: 0;
  z-index: 1000;


  /* Neon border effect */
  border: 2px solid transparent;
  box-shadow: 0 0 15px 5px rgba(255, 105, 180, 0.9), 0 0 20px 10px rgba(255, 105, 180, 0.7); /* Glowing neon effect */
}


.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}


.logo-container {
  display: flex;
  align-items: center;
}


.logo-img {
  width: 101px; /* Increased size by 45% */
  height: auto;
  margin-right: 15px; /* Space between image and text */
  transition: transform 0.3s ease;
}


.logo {
  font-size: 2.5rem;
  font-weight: bold;
  text-decoration: none;
  color: #fff;
  transition: color 0.3s ease, transform 0.3s ease;
}


.logo:hover {
  color: #ff6f61; /* Bright neon red color */
  transform: scale(1.1) rotate(-5deg);
  text-shadow: 0 0 10px #ff6f61, 0 0 20px #ff6f61; /* Glowing effect */
}


.nav-links {
  display: flex;
}


.nav-link {
  margin-left: 30px;
  color: #fff;
  text-decoration: none;
  font-size: 1.2rem;
  text-transform: uppercase;
  position: relative;
  padding: 5px 10px;
  transition: color 0.3s ease, transform 0.3s ease, text-shadow 0.3s ease;
}


.nav-link:before {
  content: "";
  position: absolute;
  width: 100%;
  height: 2px;
  background-color: #ff6f61;
  bottom: 0;
  left: 0;
  transform: scaleX(0);
  transform-origin: bottom right;
  transition: transform 0.25s ease-in-out;
}


.nav-link:hover {
  color: #db34a9; /* Vibrant neon blue */
  transform: scale(1.1) rotate(3deg);
  text-shadow: 0 0 15px #3498db, 0 0 30px #3498db; /* Glowing neon blue */
}


.nav-link:hover:before {
  transform: scaleX(1);
  transform-origin: bottom left;
}


.nav-links .nav-link {
  margin-left: 40px;
}


#nav:hover {
  background-color: #0d0d0e;
  transform: translateY(-5px); /* 3D floating effect */
}


/* Add styles for user-indicator */
.user-indicator {
  color: #fff;
  font-size: 1.1rem;
  margin-left: 20px;
}


button {
  background-color: #8e44ad;
  color: white;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 5px;
}


button:hover {
  background-color: #6a2c9c;
}


/* Responsive Design */
@media screen and (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: center;
  }


  .nav-links {
    flex-direction: column;
    margin-top: 10px;
  }


  .nav-link {
    margin: 15px 0;
  }
}


router-view {
  flex-grow: 1;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
  transition: all 0.3s ease-in-out;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeUp 1s ease forwards;
}


@keyframes fadeUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

