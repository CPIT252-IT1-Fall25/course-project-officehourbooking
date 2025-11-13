import React, { createContext, useState, useContext, useEffect } from 'react';
import authService from '../services/authService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    setUser(currentUser);
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    try{
     const response = await authService.login(credentials);
    
      setUser({
        id: response.id,
        email: response.email,
        name: response.name,
        role: response.role,
        studentId: response.studentId,
        specialty: response.specialty
    });
    return response;
    }catch (error){
      console.error(`"Login error:` ,error);
      throw error;
    }

  };

  const checkEmail = async (email) => {
    return await authService.checkEmail(email);
  };

  const signup = async (userData) => {
    const response = await authService.signup(userData);
    setUser({
      email: response.email,
      name: response.name,
      role: response.role
    });
    return response;
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  return (
  <AuthContext.Provider value={{ user, login, logout, signup, checkEmail, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
