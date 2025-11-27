import React from 'react';
import { MAX_PREVIEW_APPOINTMENTS } from '../../utils/Constants';
import { getStatusDisplayText } from '../../home/BookingStatus';
import { formatEndTime } from '../../utils/DateFormatter';
const InstructorDashboard = ({ appointments, loading, onNavigate, formatDateTime }) => {
  return (
    <>
      <div className="welcome-section">
        <h1>Instructor Dashboard</h1>
        <p>Manage your office hours and appointments</p>
      </div>

      <div className="dashboard">
        <div className="dashboard-grid">
          <div className="dashboard-card clickable" onClick={() => onNavigate('appointments')}>
            <i className="bi bi-calendar-check card-icon"></i>
            <h3>My Appointments</h3>
            <p>View and manage student bookings</p>
            <button className="btn btn-primary">
              <i className="bi bi-chevron-right"></i>
            </button>
          </div>

          <div className="dashboard-card clickable" onClick={() => onNavigate('availability')}>
            <i className="bi bi-clock-history card-icon"></i>
            <h3>Office Hours</h3>
            <p>Set your availability schedule</p>
            <button className="btn btn-primary">
              <i className="bi bi-chevron-right"></i>
            </button>
          </div>
        </div>

        <div className="appointments-section">
          <h2>Upcoming Appointments:</h2>

          {loading ? (
            <div className="loading-message">Loading appointments...</div>
          ) : appointments.length === 0 ? (
            <div className="no-appointments">
              <p>No appointments scheduled yet.</p>
            </div>
          ) : (
            <div className="appointments-list">
              {appointments.slice(0, MAX_PREVIEW_APPOINTMENTS).map((appointment) => {
                const { date, time } = formatDateTime(appointment.startTime);
                const endTime = formatEndTime(appointment.endTime);
            

                return (
                  <div key={appointment.id} className="appointment-item">
                    <div className="appointment-student">
                      {appointment.student?.name || 'Unknown Student'}
                    </div>
                    <div className="appointment-time">
                      {time} - {endTime}
                    </div>
                    <div className="appointment-location">{date}</div>
                    <div className={`appointment-status ${appointment.status?.toLowerCase()}`}>
                      {getStatusDisplayText(appointment.status)}
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default InstructorDashboard;