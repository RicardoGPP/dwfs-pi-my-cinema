import axios from 'axios';

const api = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        console.error('Request error intercepted:', error);
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        console.error('Response error intercepted:', error);

        if (error.response && error.response.status === 401) {
            //TODO: go to "unauthorized" page.
        }

        let message = error.response.data.message;

        if (!message) {
            message = error.message;
        }

        const simplifiedError = {
            status: error.response.status,
            message
        };

        return Promise.reject(simplifiedError);
    }
);

export default api;