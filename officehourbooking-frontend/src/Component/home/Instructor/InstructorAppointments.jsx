import { BookingStatus, getStatusDisplayText, isConfirmed } from '../../home/BookingStatus';
import { formatEndTime } from '../../utils/DateFormatter';

const InstructorAppointments = ({
  appointments,
  loading,
  onNavigate,
  formatDateTime,
  onCancelAppointment
}) => {
  return (
    <div className="view-container">
      <button onClick={() => onNavigate('dashboard')} className="back-button-top">
        <i className="bi bi-arrow-left"></i> Back
      </button>

      <div className="welcome-section">
        <h1>My Appointments</h1>
        <p>View and manage all your student bookings</p>
      </div>

      {loading ? (
        <div className="loading-message">Loading appointments...</div>
      ) : appointments.length === 0 ? (
        <div className="no-appointments-full">
          <i className="bi bi-calendar-x empty-icon"></i>
          <p>No appointments scheduled yet.</p>
        </div>
      ) : (
        <div className="appointments-list-full">
          {appointments.map((appointment) => {
            const { date, time } = formatDateTime(appointment.startTime);
            const endTime = formatEndTime(appointment.endTime);

            return (
              <div key={appointment.id} className="appointment-card-full">
                <div className="appointment-header">
                  <div className="student-info">
                    <i className="bi bi-person-circle"></i>
                    <div>
                      <h3>{appointment.student?.name || 'Unknown Student'}</h3>
                      <p>{appointment.student?.email || ''}</p>
                    </div>
                  </div>
                  <span className={`status-badge ${appointment.status?.toLowerCase()}`}>
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
                    <span>
                      {time} - {endTime}
                    </span>
                  </div>
                  <div className="detail-item">
                    <i className="bi bi-person-badge"></i>
                    <span>ID: {appointment.student?.universityId || 'N/A'}</span>
                  </div>
                </div>
                {appointment.status === BookingStatus.CANCELLED && (
                  <div className="cancelled-notice">
                    <i className="bi bi-exclamation-circle"></i>
                    <span>This appointment was cancelled</span>
                  </div>
                )}
                {isConfirmed(appointment.status) && (
                  <button
                    className="btn btn-danger btn-cancel"
                    onClick={() => onCancelAppointment(appointment.id)}
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
  );
};

export default InstructorAppointments;