import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const authService = {
  login: async (credentials) => {
    const response = await axios.post(`${API_URL}/auth/login`, credentials);
    console.log(response);
    if (response.data) {
      localStorage.setItem('user', JSON.stringify({
        id: response.data.id,
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
    const response = await axios.get(`${API_URL}/auth/check-email`, {
      params: { email }
    });
    return response.data;
  },

  signup: async (userData) => {
    const response = await axios.post(`${API_URL}/auth/signup`, userData);
    
    if (response.data){
      localStorage.setItem('user', JSON.stringify({
        id: response.data.id,
        email: response.data.email,
        name: response.data.name,
        role: response.data.role,
        studentId:response.data.universityId,
        specialty: response.data.specialty
      }));
    }
    
    return response.data;

  },

  logout: () => {
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }
};

// Booking related functions
export const getStudentBookings = async (studentId) => {
  const resp = await axios.get(`${API_URL}/bookings/student/${studentId}`);
  return resp.data;
};


export const getDoctorBookings = async (doctorId) => {
  const resp = await axios.get(`${API_URL}/bookings/doctor/${doctorId}`);
  return resp.data;
};
export const getDoctors = async () => {
  const resp = await axios.get(`${API_URL}/doctors`);
  return resp.data;
};

export const getDoctorAvailability = async (doctorId) => {
  const resp = await axios.get(`${API_URL}/availability/doctor/${doctorId}`);
  return resp.data;
};

export const getAvailableSlots = async (doctorId, date) => {
    const resp = await axios.get(`${API_URL}/availability/doctor/${doctorId}/slots`, {
    params: { date } // date should be in format 'YYYY-MM-DD'
    });
  return resp.data;
  };

export const getAvailableDates = async (doctorId, startDate, endDate) => {
  const resp = await axios.get(`${API_URL}/availability/doctor/${doctorId}/available-dates`, {
    params: { startDate, endDate }
  });
  return resp.data;
};

export const createBooking = async (doctorId, studentId, startTime) => {
  const resp = await axios.post(`${API_URL}/bookings`, {
    doctorId,
    studentId,
    startTime
  });
  return resp.data;
};

export const cancelBooking = async (appointmentId) => {
  const resp = await axios.put(`${API_URL}/bookings/${appointmentId}/cancel`);
  return resp.data;
};

export const deleteAvailability = async (availabilityId) => {
  const resp = await axios.delete(`${API_URL}/availability/${availabilityId}/delete`);
  return resp.data;
};

export const createAvailability = async (doctorId, dayOfWeek, startTime, endTime, slotMinutes) => {
  const resp = await axios.post(`${API_URL}/availability`, {
    doctorId,        
    dayOfWeek,      
    startTime,       
    endTime,        
    slotMinutes      
  });
  return resp.data;
};

export default authService;