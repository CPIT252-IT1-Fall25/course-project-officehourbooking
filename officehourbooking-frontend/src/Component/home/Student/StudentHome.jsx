import { useState, useEffect } from 'react';
import { useAuth } from '../../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import './StudentHome.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import BookAppointment from './BookAppointment';
import { getStudentBookings, cancelBooking } from '../../../services/authService';
import {formatDateTime , formatEndTime} from '../../utils/DateFormatter';


const StudentHome = () => {
  const { user, logout } = useAuth();
  const nav = useNavigate();
  const [view, setView] = useState('dashboard'); // 'dashboard', 'book', 'appointments', 'profile'
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (view === 'dashboard') {
      fetchAppointments();
    }
  }, [view]);

  const fetchAppointments = async () => {
    try {
      setLoading(true);
      const data = await getStudentBookings(user.id);
      setAppointments(data);
    } catch (error) {
      console.error('Error fetching appointments:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    nav('/login');
  };

  const handleBookingComplete = () => {
    setView('dashboard');
    fetchAppointments();
  };

  const handleCancelAppointment = async (appointmentId) => {
    if (!window.confirm('Are you sure you want to cancel this appointment?')) {
      return;
    }

    try {
      await cancelBooking(appointmentId);
      alert('Appointment cancelled successfully');
      fetchAppointments();
    } catch (error) {
      alert('Failed to cancel appointment. Please try again.');
    }
  };

 

  return (
    <div className="home-container">
      <nav className="navbar">
        <div className="navbar-logo">
          <h2>Office Hours Booking</h2>
        </div>
        
        <div className="navbar-links">
          <button 
            className={`nav-link ${view === 'dashboard' ? 'active' : ''}`}
            onClick={() => setView('dashboard')}
          >
            <i className="bi bi-house-door"></i> Home
          </button>
          <button 
            className={`nav-link ${view === 'appointments' ? 'active' : ''}`}
            onClick={() => setView('appointments')}
          >
            <i className="bi bi-calendar-check"></i> Appointments
          </button>
          <button 
            className={`nav-link ${view === 'profile' ? 'active' : ''}`}
            onClick={() => setView('profile')}
          >
            <i className="bi bi-person"></i> Profile
          </button>
        </div>

        <div className="navbar-menu">
          <span className="user-name">Welcome, {user?.name}</span>
          <button
            onClick={handleLogout}
            type="button"
            className="btn btn-danger"
          >
            Logout
          </button>
        </div>
      </nav>

      <div className="home-content">
        {view === 'dashboard' && (
          <>
            <div className="welcome-section">
              <h1>Student Dashboard</h1>
              <p>Book appointments with your instructors</p>
            </div>

            <div className="dashboard">
              <div className="dashboard-grid">
                <div 
                  className="dashboard-card clickable" 
                  onClick={() => setView('book')}
                >
                  <i className="bi bi-calendar-plus card-icon"></i>
                  <h3>Book Appointments</h3>
                  <p>new meeting with instructor</p>
                  <button className="btn btn-primary">
                    <i className="bi bi-chevron-right"></i>
                  </button>
                </div>
                
                <div 
                  className="dashboard-card clickable" 
                  onClick={() => setView('appointments')}
                >
                  <i className="bi bi-calendar-check card-icon"></i>
                  <h3>My Appointments</h3>
                  <p>View and manage your meetings</p>
                  <button className="btn btn-primary">
                    <i className="bi bi-chevron-right"></i>
                  </button>
                </div>
              </div>

              {/* Appointments List */}
              <div className="appointments-section">
                <h2>Appointments:</h2>
                
                {loading ? (
                  <div className="loading-message">Loading appointments...</div>
                ) : appointments.length === 0 ? (
                  <div className="no-appointments">
                    <p>No appointments scheduled yet.</p>
                  </div>
                ) : (
                  <div className="appointments-list">
                    {appointments.map((appointment) => {
                      const { date, time } = formatDateTime(appointment.startTime);
                      const endTime = formatEndTime(appointment.endTime);
                      
                      return (
                        <div key={appointment.id} className="appointment-item">
                          <div className="appointment-doctor">
                            Dr. {appointment.doctor.name}
                          </div>
                           <div className="appointment-date">
                            {date}
                          </div>
                          <div className="appointment-time">
                            {time} - {endTime}
                          </div>
                          <div className="appointment-location">
                            location: {'80A'}
                          </div>
                          <div className={`appointment-status ${appointment.status.toLowerCase()}`}>
                            {appointment.status === 'CONFIRMED' ? 'Confirmed' : 'Canceled'}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                )}
              </div>
            </div>
          </>
        )}

        {view === 'book' && (
          <div className="view-container">
            <BookAppointment 
              studentId={user.id} 
              onBack={handleBookingComplete}
            />
          </div>
        )}

        {view === 'appointments' && (
          <div className="view-container">
            <button 
              onClick={() => setView('dashboard')} 
              className="back-button-top"
            >
              <i className="bi bi-arrow-left"></i> Back
            </button>

            <div className="welcome-section">
              <h1>My Appointments</h1>
              <p>View and manage your scheduled meetings</p>
            </div>

            {loading ? (
              <div className="loading-message">Loading appointments...</div>
            ) : appointments.length === 0 ? (
              <div className="no-appointments-full">
                <i className="bi bi-calendar-x empty-icon"></i>
                <p>You don't have any appointments yet.</p>
                <button 
                  className="btn btn-primary"
                  onClick={() => setView('book')}
                >
                  Book Your First Appointment
                </button>
              </div>
            ) : (
              <div className="appointments-list-full">
                {appointments.map((appointment) => {
                  const { date, time } = formatDateTime(appointment.startTime);
                  const endTime = formatEndTime(appointment.endTime);
                  
                  return (
                    <div key={appointment.id} className="appointment-card-full">
                      <div className="appointment-header">
                        <div className="doctor-info">
                          <i className="bi bi-person-circle"></i>
                          <div>
                            <h3>Dr. {appointment.doctor.name}</h3>
                            <p>{appointment.doctor.specialty}</p>
                          </div>
                        </div>
                        <span className={`status-badge ${appointment.status.toLowerCase()}`}>
                          {appointment.status}
                        </span>
                      </div>
                      <div className="appointment-details">
                        <div className="detail-item">
                          <i className="bi bi-calendar3"></i>
                          <span>{date}</span>
                        </div>
                        <div className="detail-item">
                          <i className="bi bi-clock"></i>
                          <span>{time} - {endTime}</span>
                        </div>
                        <div className="detail-item">
                          <i className="bi bi-geo-alt"></i>
                          <span>{'80A'}</span>
                        </div>
                      </div>
                      {appointment.status === 'CANCELLED' && (
                        <div className="cancelled-notice">
                          <i className="bi bi-exclamation-circle"></i>
                          <span>This appointment was cancelled</span>
                        </div>
                      )}
                      {appointment.status === 'CONFIRMED' && (
                        <button
                          className="btn btn-danger btn-cancel"
                          onClick={() => handleCancelAppointment(appointment.id)}
                        >
                          <i className="bi bi-x-circle"></i> Cancel Appointment
                        </button>
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        )}

        {view === 'profile' && (
          <div className="view-container">
            <button 
              onClick={() => setView('dashboard')} 
              className="back-button-top"
            >
              <i className="bi bi-arrow-left"></i> Back
            </button>

            <div className="welcome-section">
              <h1>My Profile</h1>
              <p>View and manage your account information</p>
            </div>

            <div className="profile-card">
              <div className="profile-icon">
                <i className="bi bi-person-circle"></i>
              </div>
              <div className="profile-info">
                <div className="info-item">
                  <label>Name:</label>
                  <span>{user?.name}</span>
                </div>
                <div className="info-item">
                  <label>Email:</label>
                  <span>{user?.email}</span>
                </div>
                <div className="info-item">
                  <label>Student ID:</label>
                  <span>{user?.studentId || 'N/A'}</span>
                </div>
                <div className="info-item">
                  <label>Role:</label>
                  <span>{user?.role}</span>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default StudentHome;