<template>
  <div id="login" class="login-container">
    <form v-on:submit.prevent="login" class="fade-up">
      <h1 class="form-title rainbow-text">Please Sign In</h1>
      <div role="alert" v-if="invalidCredentials" class="error-message fade-up rainbow-text">
        Invalid username and password!
      </div>
      <div role="alert" v-if="this.$route.query.registration" class="error-message fade-up rainbow-text">
        Thank you for registering, please sign in.
      </div>
      <div class="form-input-group fade-up">
        <label for="username" class="rainbow-text">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group fade-up">
        <label for="password" class="rainbow-text">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
     
      <!-- Centered Button Container -->
      <div class="button-container">
        <button type="submit" class="submit-button">Sign in</button>
      </div>


      <p><router-link v-bind:to="{ name: 'register' }" class="register-link fade-up rainbow-text">Need an account? Sign up.</router-link></p>
    </form>
  </div>
</template>


<script>
import authService from "../services/AuthService";


export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status === 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/home");
          }
        })
        .catch((error) => {
          const response = error.response;
          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    },
  },
};
</script>


<style scoped>
#login {
  background-color: #1a1a1a;
  color: #fff;
  font-family: 'Roboto', sans-serif;
  padding: 50px 20px;
  border-radius: 10px;
  width: 100%;
  max-width: 850px;
  max-height: 950px;
  margin: 0 auto;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}


@keyframes rainbow {
  0% {
    color: #ff0000;
  }
  14% {
    color: #ff7f00;
  }
  28% {
    color: #ffff00;
  }
  42% {
    color: #00ff00;
  }
  57% {
    color: #0000ff;
  }
  71% {
    color: #4b0082;
  }
  85% {
    color: #8b00ff;
  }
  100% {
    color: #ff0000;
  }
}


.rainbow-text {
  animation: rainbow 5s linear infinite;
}


.form-title {
  font-size: 2rem;
  margin-bottom: 20px;
  color: #ecf0f1;
  animation: bounce 2s infinite;
}


.form-input-group {
  margin-bottom: 1.5rem;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeUp 1s ease forwards;
}


label {
  font-size: 1.2rem;
  color: #ecf0f1;
  margin-bottom: 5px;
  display: block;
  animation: bounce 2s infinite;
}


input {
  width: 95%;
  padding: 15px;
  background-color: #2e2847;
  border: 2px solid #2c3e50;
  border-radius: 5px;
  color: #fff;
  font-size: 1.1rem;
  outline: none;
  transition: all 0.3s ease;
}


input:focus {
  border-color: #3498db;
  box-shadow: 0 0 8px rgba(52, 152, 219, 0.8);
}


.error-message {
  background-color: #e74c3c;
  color: #fff;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 20px;
  font-size: 1.1rem;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeUp 1s ease forwards;
  animation: bounce 2s infinite;
}


.submit-button {
  width: 70%; /* Adjust the width of the button */
  padding: 15px;
  background: transparent;
  color: #fff;
  border: 2px solid #3498db;
  border-radius: 50px;
  font-size: 1.2rem;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease, background 0.3s ease;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  animation: pulse 1.5s infinite;
}


.submit-button::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 300%;
  height: 300%;
  background: rgba(52, 152, 219, 0.5);
  transition: all 0.3s ease;
  border-radius: 50%;
  transform: translate(-50%, -50%) scale(0);
  z-index: -1;
}


.submit-button:hover {
  background: linear-gradient(45deg, #ff6b81, #3498db);
  transform: scale(1.1);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
}


.submit-button:hover::before {
  transform: translate(-50%, -50%) scale(1);
}


.submit-button:active {
  transform: scale(1);
}


.register-link {
  color: #ecf0f1;
  text-decoration: none;
  font-size: 1.1rem;
  transition: color 0.3s ease;
  animation: rainbow 5s linear infinite;
}


.register-link:hover {
  color: #3498db;
}


@keyframes fadeUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}


@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}


@keyframes pulse {
  0% {
    transform: scale(1);
    background-color: #3498db;
  }
  50% {
    transform: scale(1.1);
    background-color: #ff6b81;
  }
  100% {
    transform: scale(1);
    background-color: #3498db;
  }
}


/* Centering Button Container */
.button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}
</style>
