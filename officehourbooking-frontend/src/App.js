import './App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router';
import Login from './Component/auth/Login';
import PrivateRoute from './Component/utils/PrivateRoute';
import { AuthProvider } from './context/AuthContext';
import InstructorHome from './Component/home/Instructor/InstructorHome'
import StudentHome from './Component/home/Student/StudentHome'
import SignUp from './Component/auth/signup';
import { NotificationProvider } from './context/NotificationContext';

function App() {
  return (
    <AuthProvider>
       <NotificationProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
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
      </Router>
      </NotificationProvider>
    </AuthProvider>
  );
}

export default App;
