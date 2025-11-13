import './App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router';
import Login from './Component/auth/Login';
import PrivateRoute from './Component/utilt/PrivateRoute';
import { AuthProvider } from './context/AuthContext';
import InstructorHome from './Component/home/InstructorHome'
import StudentHome from './Component/home/StudentHome'
import SignUp from './Component/auth/signup';

function App() {
  return (

   <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={ <Login /> } />
          <Route path="/signup" element={<SignUp />} />
          
          <Route 
            path="/StudentHome" 
            element={
              <PrivateRoute allowedRoles={['STUDENT']}>
                <StudentHome />
              </PrivateRoute>
            } 
          />
          
          <Route 
            path="/InstructorHome" 
            element={
              <PrivateRoute allowedRoles={['DOCTOR']}>
                <InstructorHome />
              </PrivateRoute>
            } 
          />
          
          <Route path="/" element={<Navigate to="/login" replace />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
