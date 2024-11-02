import api from '@/services/api';

const RESOURCE = '/movies';

const movieService = {

    async getAll(nowShowing = false) {
        const response = await api.get(RESOURCE, {
            params: {
                'now-showing': nowShowing
            }
        });
        return response.data;
    },

    async getById(id) {
        const response = await api.get(`${RESOURCE}/${id}`);
        return response.data;
    },

    async create(movie) {
        const response = await api.post(RESOURCE, movie);
        return response.data;
    },

    async update(id, movie) {
        const response = await api.put(`${RESOURCE}/${id}`, movie);
        return response.data;
    },

    async delete(id) {
        const response = await api.delete(`${RESOURCE}/${id}`);
        return response.data;
    },

    async getMovieOptions(name) {
        const response = await api.get(`${RESOURCE}/options`, {
            params: {
                name
            }
        });
        return response.data;
    },

    async getMovieByMovieOptionId(movieOptionId) {
        const response = await api.get(`${RESOURCE}/options/${movieOptionId}`);
        return response.data;
    }
};

export default movieService;