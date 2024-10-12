import axios from 'axios';

const api = axios.create({
    baseURL: 'http://127.0.0.1:9080',
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

        return Promise.reject(error);
    }
);

export default api;