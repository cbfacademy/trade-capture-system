import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});


export const fetchTrades = () => api.get('/trades');

export const fetchAllUsers = async () => {
  console.log("Fetching all users from the API");
  return await api.get('/users').then((res) => {return res});
};

export const createUser = (user) => api.post('/users', user);

export const fetchUserProfiles = () => api.get('/userProfiles');

export const updateUser = (id, user) => api.put(`/users/${id}`, user);

export const authenticate = (user: string, pass: string) => {
  return api.post(`/login/${user}`, null, {
    params: {
      Authorization: pass
    }
  });
}

export const getUserByLogin = (login: string) => {
    return api.get(`/users/loginId/${login}`);
}
export default api;

