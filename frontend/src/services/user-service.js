import api from '@/services/api';

const RESOURCE = '/users';

const userService = {

    async getAll() {
        const response = await api.get(RESOURCE);
        return response.data;
    },

    async getById(id) {
        const response = await api.get(`${RESOURCE}/${id}`);
        return response.data;
    },

    async create(user) {
        const response = await api.post('/users', user);
        return response.data;
    },

    async update(id, user) {
        const response = await api.put(`${RESOURCE}/${id}`, user);
        return response.data;
    },

    async delete(id) {
        const response = await api.delete(`${RESOURCE}/${id}`);
        return response.data;
    },

    async login(credentials) {
        const response = await api.post(`${RESOURCE}/login`, credentials);
        return response.data.token;
    }
};

export default userService;