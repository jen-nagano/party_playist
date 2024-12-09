import axios from 'axios';
const http = axios.create({
  baseURL: 'https://api.spotify.com/v1',
});
const token = 'BQBIW1CVnbFwtZbdSKJJeY8TaD2_qbFFbbEGLhCceWcBAjBQtrZg3IQS0YbhrrQGkGhr-FEKCOZbaMZ8tZUAhPTxb5toSt6-U3XXfLxR7OsWctIjy4I'; // Replace with your actual token
http.defaults.headers.common['Authorization'] = `Bearer ${token}`;
export default {
  getTrack(trackId) {
    return http.get(`/tracks/${trackId}`);
  },
  searchTrack(query) {
    return http.get(`/search`, {
      params: {
        q: query,
        type: 'track',
        limit: 10, // Limit results to 10 tracks
      },
    });
  },
};