import api from '@/services/api';

const RESOURCE = '/locations';

const locationService = {

    async getAll() {
        const response = await api.get(RESOURCE);
        return response.data;
    },

    async getById(id) {
        const response = await api.get(`${RESOURCE}/${id}`);
        return response.data;
    },

    async create(location) {
        const response = await api.post(RESOURCE, location);
        return response.data;
    },

    async update(id, location) {
        const response = await api.put(`${RESOURCE}/${id}`, location);
        return response.data;
    },

    async delete(id) {
        const response = await api.delete(`${RESOURCE}/${id}`);
        return response.data;
    }
};

export default locationService;