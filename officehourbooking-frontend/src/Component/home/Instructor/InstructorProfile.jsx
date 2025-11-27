import React from 'react';
import { getRoleDisplayText } from '../../utils/Roles';

const InstructorProfile = ({ user, onNavigate }) => {
  return (
    <div className="view-container">
      <button onClick={() => onNavigate('dashboard')} className="back-button-top">
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
            <label>Specialty:</label>
            <span>{user?.specialty || 'N/A'}</span>
          </div>
          <div className="info-item">
            <label>Role:</label>
            <span>{getRoleDisplayText(user?.role)}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default InstructorProfile;