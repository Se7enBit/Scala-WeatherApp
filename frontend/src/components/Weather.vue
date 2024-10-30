<template>
  <div class="weather-container">
    <h1>Weather Checker</h1>
    <input
      v-model="city"
      type="text"
      placeholder="Enter city name"
      @keyup.enter="getWeather"
    />
    <button @click="getWeather">Get Weather</button>

    <div v-if="loading">Loading...</div>
    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="weather" class="weather-info">
      <h2>Weather in {{ city }}</h2>
      <p>Data: {{ weather.data }} °C</p>
    </div>
  </div>
</template>

<script>

import axios from "axios";

export default {
  data() {
    return {
      city: "", // City input by the user
      weather: null, // Weather data
      loading: false, // Loading state
      error: null, // Error message
    };
  },
  methods: {
    async getWeather() {
      // Reset state before fetching
      this.weather = null;
      this.error = null;
      this.loading = true;

      try {
        const response = await axios.get(
          `http://localhost:9000/weather?city=${this.city}`
        );

        // Parse the response to extract weather details
        this.weather = {
          data : response.data,
          };
      } catch (err) {
        console.error("Error fetching weather data:", err);
        this.error = "Could not fetch weather data. Please try again.";
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.weather-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
  font-family: Arial, sans-serif;
}

input {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
}

button {
  padding: 8px 12px;
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.weather-info {
  margin-top: 20px;
}

.error {
  color: red;
  margin-top: 10px;
}
</style>

