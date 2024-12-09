<template>
  <div id="register" class="text-center">
    <form v-on:submit.prevent="register" class="fade-up">
      <!-- Title with rainbow text -->
      <h1 class="form-title rainbow-text">Create Account</h1>


      <!-- Error Messages -->
      <div role="alert" v-if="registrationErrors" class="error-message fade-up">
        {{ registrationErrorMsg }}
      </div>


      <!-- Form Input Groups -->
      <div class="form-input-group fade-up">
        <label for="username" class="rainbow-text">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group fade-up">
        <label for="password" class="rainbow-text">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <div class="form-input-group fade-up">
        <label for="confirmPassword" class="rainbow-text">Confirm Password</label>
        <input type="password" id="confirmPassword" v-model="user.confirmPassword" required />
      </div>


      <!-- Centered Button Container -->
      <div class="button-container">
        <button type="submit" class="submit-button fade-up">Create Account</button>
      </div>


      <!-- Link to Login -->
      <p><router-link v-bind:to="{ name: 'login' }" class="login-link fade-up">Already have an account? Log in.</router-link></p>
    </form>
  </div>
</template>


<script>
import authService from '../services/AuthService';


export default {
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else if (this.user.password.length < 8 || !/[^a-zA-Z0-9]/.test(this.user.password) || !/[a-z]/.test(this.user.password) || !/[A-Z]/.test(this.user.password) || !/[0-9]/.test(this.user.password)) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Invalid password. Password must be at least 8 characters long and contain a lowercase letter, an uppercase letters, and a special character.';
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: '/login',
                query: { registration: 'success' },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
};
</script>


<style scoped>
/* Global Dark Theme */
#register {
  background-color: #1a1a1a; /* Dark background */
  color: #fff;
  font-family: 'Roboto', sans-serif;
  padding: 50px 20px;
  border-radius: 10px;
  width: 100%;
  max-width: 850px;
  margin: 0 auto;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}


/* Title Styling */
.form-title {
  font-size: 2rem;
  margin-bottom: 20px;
  color: #ecf0f1;
  animation: bounce 2s infinite; /* Bounce effect */
}


/* Rainbow Animation for Text */
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
  animation: rainbow 5s linear infinite; /* Apply rainbow text animation */
}


/* Form Input Groups */
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
  animation: bounce 2s infinite; /* Apply bounce to the label */
}


input {
  width: 95%;
  padding: 12px;
  background-color: #34495e;
  border: 2px solid #2c3e50;
  border-radius: 5px;
  color: #fff;
  font-size: 1rem;
  outline: none;
  transition: all 0.3s ease;
}


input:focus {
  border-color: #3498db;
  box-shadow: 0 0 8px rgba(52, 152, 219, 0.8);
}


/* Error Message */
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
}


/* Submit Button */
.submit-button {
  width: 70%;
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


/* Centering Button Container */
.button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  width: 100%;
}


/* Link to Login */
.login-link {
  color: #ecf0f1;
  text-decoration: none;
  font-size: 1.1rem;
  transition: color 0.3s ease;
}


.login-link:hover {
  color: #3498db;
}


/* Fade-Up Animation */
@keyframes fadeUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}


/* Bounce Animation */
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


/* Pulse Animation for the button */
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
</style>
