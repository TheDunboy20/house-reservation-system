import axios from 'axios';

const api = axios.create({
  baseURL: '',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Auth API
export const authAPI = {
  register: async (username, password) => {
    const response = await api.post('/auth/register', { username, password });
    return response.data;
  },

  login: async (username, password) => {
    const formData = new URLSearchParams();
    formData.append('username', username);
    formData.append('password', password);

    const response = await api.post('/auth/login', formData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    return response.data;
  },

  logout: async () => {
    await api.post('/auth/logout');
  },
};

// House API
export const houseAPI = {
  getAll: async () => {
    const response = await api.get('/houses');
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/houses/${id}`);
    return response.data;
  },

  create: async (houseData) => {
    const response = await api.post('/houses', houseData);
    return response.data;
  },

  update: async (id, houseData) => {
    const response = await api.put(`/houses/${id}`, houseData);
    return response.data;
  },

  delete: async (id) => {
    const response = await api.delete(`/houses/${id}`);
    return response.data;
  },

  getHouseDays: async (id) => {
    const response = await api.get(`/houses/${id}/house-days`);
    return response.data;
  },
};

// Reservation API
export const reservationAPI = {
  getAll: async () => {
    const response = await api.get('/reservations');
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/reservations/${id}`);
    return response.data;
  },

  create: async (houseDayId) => {
    const response = await api.post(`/reservations/${houseDayId}`);
    return response.data;
  },

  update: async (id, reservationData) => {
    const response = await api.put(`/reservations/${id}`, reservationData);
    return response.data;
  },

  delete: async (id) => {
    const response = await api.delete(`/reservations/${id}`);
    return response.data;
  },

  deleteByHouseDayId: async (houseDayId) => {
    const response = await api.delete(`/reservations/${houseDayId}`);
    return response.data;
  },
};

export default api;
