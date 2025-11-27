import React from 'react';

const InstructorNavbar = ({ user, view, onNavigate, onLogout }) => {
  return (
    <nav className="navbar">
      <div className="navbar-logo">
        <h2>Office Hours Booking</h2>
      </div>

      <div className="navbar-links">
        <button
          className={`nav-link ${view === 'dashboard' ? 'active' : ''}`}
          onClick={() => onNavigate('dashboard')}
        >
          <i className="bi bi-house-door"></i> Home
        </button>
        <button
          className={`nav-link ${view === 'appointments' ? 'active' : ''}`}
          onClick={() => onNavigate('appointments')}
        >
          <i className="bi bi-calendar-check"></i> Appointments
        </button>
        <button
          className={`nav-link ${view === 'availability' ? 'active' : ''}`}
          onClick={() => onNavigate('availability')}
        >
          <i className="bi bi-clock-history"></i> Availability
        </button>
        <button
          className={`nav-link ${view === 'profile' ? 'active' : ''}`}
          onClick={() => onNavigate('profile')}
        >
          <i className="bi bi-person"></i> Profile
        </button>
      </div>

      <div className="navbar-menu">
        <span className="user-name">Welcome, {user?.name}</span>
        <button onClick={onLogout} type="button" className="btn btn-danger">
          Logout
        </button>
      </div>
    </nav>
  );
};

export default InstructorNavbar;