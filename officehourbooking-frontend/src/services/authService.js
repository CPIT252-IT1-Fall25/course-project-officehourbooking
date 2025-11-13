import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';


const authService = {
  login: async (credentials) => {
    const response = await axios.post(`${API_URL}/login`, credentials);
    console.log(response)
    if(response.data){
      localStorage.setItem(`user`,JSON.stringify({
        id:response.data.id,
        email: response.data.email,
        name: response.data.name,
        role: response.data.role,
        studentId: response.data.universityId,
        specialty: response.data.specialty
      }));
    }
    
    return response.data;
  },

  checkEmail: async (email) => {
    const response = await axios.get(`${API_URL}/check-email`,{
      params: {email}
    });
    return response.data;
  },

  signup: async (userData) => {
  const response = await axios.post(`${API_URL}/signup`, userData);
  return response.data; // string message like "Signup successful"
},

  logout: () => {
    localStorage.removeItem(`user`);
  },

  getCurrentUser: () => {
    const userStr = localStorage.getItem(`user`);
    return userStr ? JSON.parse(userStr) : null;
  }
};


export default authService;