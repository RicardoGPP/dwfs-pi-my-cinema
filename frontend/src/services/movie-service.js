import api from '@/services/api';

const RESOURCE = '/movies';

const movieService = {

    async getAll() {
        const response = await api.get(RESOURCE);
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

    async getPreview(name) {
        const response = await api.get(`${RESOURCE}/preview`, {
            params: {
                name
            }
        });
        return response.data;
    }
};

export default movieService;