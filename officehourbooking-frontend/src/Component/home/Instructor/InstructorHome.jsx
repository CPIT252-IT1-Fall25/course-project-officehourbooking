import  { useState, useEffect } from 'react';
import { useAuth } from '../../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './InstructorHome.css';
import {
  getDoctorBookings,
  cancelBooking,
  deleteAvailability,
  getDoctorAvailability,
  createAvailability
} from '../../../services/authService';

// Import components
import InstructorNavbar from './InstructorNavbar';
import InstructorDashboard from './InstructorDashboard';
import InstructorAppointments from './InstructorAppointments';
import AvailabilityManager from './AvailabilityManager';
import InstructorProfile from './InstructorProfile';

// Import utilities
import { formatDateTime, formatDayTime } from '../../utils/DateFormatter';

// Import constants
import {
  DEFAULT_SLOT_MINUTES,
  DEFAULT_START_TIME,
  DEFAULT_END_TIME,
  MINUTES_PER_HOUR
} from '../availability';

const InstructorHome = () => {
  const { user, logout } = useAuth();
  const nav = useNavigate();
  const [view, setView] = useState('dashboard');
  const [appointments, setAppointments] = useState([]);
  const [availability, setAvailability] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);

  const [formData, setFormData] = useState({
    dayOfWeek: '',
    startTime: DEFAULT_START_TIME,
    endTime: DEFAULT_END_TIME,
    slotMinutes: DEFAULT_SLOT_MINUTES
  });

  const [formError, setFormError] = useState('');
  const [formLoading, setFormLoading] = useState(false);

  useEffect(() => {
    if (view === 'dashboard' || view === 'appointments') {
      fetchAppointments();
    } else if (view === 'availability') {
      fetchAvailability();
    }
  }, [view]);

  const fetchAppointments = async () => {
    try {
      setLoading(true);
      const data = await getDoctorBookings(user.id);
      setAppointments(data || []);
    } catch (err) {
      console.error('Error fetching Doctor Appointments:', err);
      setAppointments([]);
    } finally {
      setLoading(false);
    }
  };

  const fetchAvailability = async () => {
    try {
      setLoading(true);
      const data = await getDoctorAvailability(user.id);
      setAvailability(data || []);
    } catch (err) {
      console.error('Error fetching Doctor Availability:', err);
      setAvailability([]);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    nav('/login');
  };

  const handleCancelAppointment = async (appointmentId) => {
    if (!window.confirm('Are you sure you want to cancel this appointment?')) {
      return;
    }

    try {
      await cancelBooking(appointmentId);
      alert('Appointment cancelled.');
      fetchAppointments();
    } catch (err) {
      console.error('Error cancelling appointment:', err);
      alert('Failed to cancel appointment.');
    }
  };

  const handleDeleteAvailability = async (availabilityId) => {
    if (!window.confirm('Are you sure you want to delete this availability?')) {
      return;
    }

    try {
      await deleteAvailability(availabilityId);
      alert('Availability deleted successfully');
      fetchAvailability();
    } catch (err) {
      console.error('Error deleting availability:', err);
      alert('Failed to delete availability');
    }
  };

  const calculateSlots = () => {
    if (!formData.startTime || !formData.endTime || !formData.slotMinutes) return 0;

    const [startHour, startMin] = formData.startTime.split(':').map(Number);
    const [endHour, endMin] = formData.endTime.split(':').map(Number);

    const startMinutes = startHour * MINUTES_PER_HOUR + startMin;
    const endMinutes = endHour * MINUTES_PER_HOUR + endMin;

    if (endMinutes <= startMinutes) return 0;

    return Math.floor((endMinutes - startMinutes) / formData.slotMinutes);
  };

  const handleFormChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    setFormError('');
  };

  const validateForm = () => {
    if (!formData.dayOfWeek) {
      setFormError('Please select a day');
      return false;
    }

    if (!formData.startTime || !formData.endTime) {
      setFormError('Please select times');
      return false;
    }

    const [startHour, startMin] = formData.startTime.split(':').map(Number);
    const [endHour, endMin] = formData.endTime.split(':').map(Number);
    const startMinutes = startHour * MINUTES_PER_HOUR + startMin;
    const endMinutes = endHour * MINUTES_PER_HOUR + endMin;

    if (endMinutes <= startMinutes) {
      setFormError('End time must be after start time.');
      return false;
    }

    if (calculateSlots() < 1) {
      setFormError('Time range must allow at least one slot.');
      return false;
    }

    return true;
  };

  const handleAddAvailability = async () => {
    if (!validateForm()) return;

    setFormLoading(true);
    setFormError('');

    try {
      await createAvailability(
        user.id,
        formData.dayOfWeek,
        formData.startTime,
        formData.endTime,
        formData.slotMinutes
      );

      alert('Availability added successfully!');
      setShowAddForm(false);
      setFormData({
        dayOfWeek: '',
        startTime: DEFAULT_START_TIME,
        endTime: DEFAULT_END_TIME,
        slotMinutes: DEFAULT_SLOT_MINUTES
      });
      fetchAvailability();
    } catch (err) {
      const errmsg = err.response?.data?.message || 'Failed to add availability.';
      setFormError(errmsg);
    } finally {
      setFormLoading(false);
    }
  };

  const handleCancelForm = () => {
    setShowAddForm(false);
    setFormData({
      dayOfWeek: '',
      startTime: DEFAULT_START_TIME,
      endTime: DEFAULT_END_TIME,
      slotMinutes: DEFAULT_SLOT_MINUTES
    });
    setFormError('');
  };

  return (
    <div className="home-container">
      <InstructorNavbar user={user} view={view} onNavigate={setView} onLogout={handleLogout} />

      <div className="home-content">
        {view === 'dashboard' && (
          <InstructorDashboard
            appointments={appointments}
            loading={loading}
            onNavigate={setView}
            formatDateTime={formatDateTime}
          />
        )}

        {view === 'appointments' && (
          <InstructorAppointments
            appointments={appointments}
            loading={loading}
            onNavigate={setView}
            formatDateTime={formatDateTime}
            onCancelAppointment={handleCancelAppointment}
          />
        )}

        {view === 'availability' && (
          <AvailabilityManager
            availability={availability}
            loading={loading}
            showAddForm={showAddForm}
            setShowAddForm={setShowAddForm}
            formData={formData}
            formError={formError}
            formLoading={formLoading}
            onNavigate={setView}
            onFormChange={handleFormChange}
            onAddAvailability={handleAddAvailability}
            onCancelForm={handleCancelForm}
            onDeleteAvailability={handleDeleteAvailability}
            calculateSlots={calculateSlots}
            formatDayTime={formatDayTime}
          />
        )}

        {view === 'profile' && <InstructorProfile user={user} onNavigate={setView} />}
      </div>
    </div>
  );
};

export default InstructorHome;