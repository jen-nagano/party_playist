<template>
    <form v-on:submit.prevent="submitForm" class="eventForm">
      <div class="form-group">
        <label for="event-name">Event Name:</label>
        <input id="event-name" type="text" class="form-control" v-model="editEvent.name" autocomplete="off" />
      </div>
      <div class="form-group">
        <label for="event-date">Date:</label>
        <input id="event-date" type="date" v-model="editEvent.date" />
      </div>
      <div class="form-group">
        <label for="start-time">Start Time:</label>
        <input id="start-time" type="time" v-model="editEvent.startTime" />
      </div>
      <div class="form-group">
        <label for="end-time">End Time:</label>
        <input id="end-time" type="time" v-model="editEvent.endTime" />
      </div>
      <!-- <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" class="form-control" v-model="editEvent.description"></textarea>
      </div> -->
      <button class="btn btn-submit">Submit</button>
      <button class="btn btn-cancel" v-on:click="cancelForm" type="button">Cancel</button>
    </form>
  </template>
  
  <script>
  import EventService from '../services/EventService';
  
  export default {
    props: {

    },
    data() {
      return {
        // Since props are read-only, don't bind to 'card' directly. Instead, create
        // a new object initialized with the same property values.
        editEvent: {
          name: '',
          date: {},
          startTime: {},
          endTime:{},
          creator: this.$store.state.user.id
        },
      };
    },
    methods: {
      submitForm() {
        // if (!this.validateForm()) {
        //   return;
        // }
  
        //ADD EVENT is handled here
            console.log('adding event: '+ this.editEvent);
          // add
          EventService
            .addEvent(this.editEvent)
            .then(response => {
              if (response.status === 201) {
                // this.$store.commit(
                //   'SET_NOTIFICATION',
                //   {
                //     message: 'A new event was added.',
                //     type: 'success'
                //   }
                // );
                // this.$router.push({ name: 'BoardView', params: { id: this.editCard.boardId } });
              }
            })
            .catch(error => {
              this.handleErrorResponse(error, 'adding');
            });
        
            this.$emit('formSubmitted');
        // EDIT EVENT is handled here
        // else {
        //   EventService
        //     .updateEvent(this.editCard)
        //     .then(response => {
        //       if (response.status === 200) {
        //         this.$store.commit(
        //           'SET_NOTIFICATION',
        //           {
        //             message: `Card ${this.editCard.id} was updated.`,
        //             type: 'success'
        //           }
        //         );
        //         this.$router.push({ name: 'BoardView', params: { id: this.editCard.boardId } });
        //       }
        //     })
        //     .catch(error => {
        //       this.handleErrorResponse(error, 'updating');
        //     });
        // }
      },
      cancelForm() {
        this.$router.push({ name: 'BoardView', params: { id: this.editCard.boardId } });
      },
      handleErrorResponse(error, verb) {
        // if (error.response) {
        //   this.$store.commit('SET_NOTIFICATION',
        //     "Error " + verb + " card. Response received was '" + error.response.statusText + "'.");
        // } else if (error.request) {
        //   this.$store.commit('SET_NOTIFICATION', "Error " + verb + " card. Server could not be reached.");
        // } else {
        //   this.$store.commit('SET_NOTIFICATION', "Error " + verb + " card. Request could not be created.");
        // }
      },
      validateForm() {
        let msg = '';
        if (this.editCard.title.length === 0) {
          msg += 'The new card must have a title. ';
        }
        if (this.editCard.status.length === 0) {
          msg += 'The new card must have a status.';
        }
        if (msg.length > 0) {
          this.$store.commit('SET_NOTIFICATION', msg);
          return false;
        }
        return true;
      },
    },
  };
  </script>
  
  
  <style scoped>
  .cardForm {
    padding: 10px;
    margin-bottom: 10px;
  }
  
  .form-group {
    margin-bottom: 10px;
    margin-top: 10px;
  }
  
  label {
    display: inline-block;
    margin-bottom: 0.5rem;
  }
  
  .form-control {
    display: block;
    width: 80%;
    height: 30px;
    padding: 0.375rem 0.75rem;
    font-size: 1rem;
    font-weight: 400;
    line-height: 1.5;
    color: #495057;
    border: 1px solid #ced4da;
    border-radius: 0.25rem;
  }
  
  textarea.form-control {
    height: 75px;
    font-family: Arial, Helvetica, sans-serif;
  }
  
  select.form-control {
    width: 20%;
    display: inline-block;
    margin: 10px 20px 10px 10px;
  }
  
  </style>
  