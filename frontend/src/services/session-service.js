import api from '@/services/api';

const RESOURCE = '/sessions';

const sessionService = {

    async getAll(movieId = null) {
        const response = await api.get(RESOURCE, {
            params: { 'movie-id': movieId }
        });
        return response.data;
    },

    async getById(id) {
        const response = await api.get(`${RESOURCE}/${id}`);
        return response.data;
    },

    async create(session) {
        const response = await api.post(RESOURCE, session);
        return response.data;
    },

    async update(id, session) {
        const response = await api.put(`${RESOURCE}/${id}`, session);
        return response.data;
    },

    async delete(id) {
        const response = await api.delete(`${RESOURCE}/${id}`);
        return response.data;
    }
};

export default sessionService;