import api from '@/services/api';

const RESOURCE = '/comments';

const commentService = {

    async getAll(movieId = null) {
        const response = await api.get(RESOURCE, {
            params: { 'movie-id': movieId }
        });
        return response.data;
    },

    async getSummary(movieId) {
        const response = await api.get(`${RESOURCE}/summary`, {
            params: { 'movie-id': movieId }
        });
        return response.data.summary;
    },

    async create(comment) {
        const response = await api.post(RESOURCE, comment);
        return response.data;
    },

    async delete(id) {
        const response = await api.delete(`${RESOURCE}/${id}`);
        return response.data;
    }
};

export default commentService;