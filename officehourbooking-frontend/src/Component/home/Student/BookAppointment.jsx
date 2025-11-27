import React, { useEffect, useState } from 'react';
import { getDoctors,getDoctorAvailability, getAvailableSlots, createBooking, getAvailableDates } from '../../../services/authService';
import { useNotification } from '../../../context/NotificationContext';
import './BookAppointment.css';
import { formatTimeSlot } from '../../utils/DateFormatter'
// Person Icon Component
const PersonIcon = ({ className = "doctor-icon" }) => (
  <svg
    className={className}
    fill="currentColor"
    viewBox="0 0 24 24"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
  </svg>
);

const BookAppointment = ({ onBack }) => {
  const [instructors, setInstructors] = useState([]);
  const [selectedInstructor, setSelectedInstructor] = useState(null);
  const [availableSlots, setAvailableSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().slice(0, 10));
  const [availableDates, setAvailableDates] = useState([]);
  const [instructorAvailability, setInstructorAvailability] = useState([]);
  const [loadingInstructors, setLoadingInstructors] = useState(true);
  const [loadingSlots, setLoadingSlots] = useState(false);
  const [error, setError] = useState(null);

  const { showSuccess, showError, showInfo } = useNotification();

  // Get student from localStorage
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const studentId = user.id;

  useEffect(() => {
    const fetchInstructors = async () => {
      try {
        const inst = await getDoctors();
        setInstructors(inst);
      } catch (err) {
        setError('Failed loading instructors.');
      } finally {
        setLoadingInstructors(false);
      }
    };
    fetchInstructors();
  }, []);

  useEffect(() => {
    const fetchAvailableDates = async () => {
      if (!selectedInstructor) return;
      
      try {
        // get instructor's availability schedule
        const availability = await getDoctorAvailability(selectedInstructor.id);
        setInstructorAvailability(availability);
        
        //get specific dates for the calendar
        const today = new Date();
        const startDate = today.toISOString().slice(0, 10);
        const endDate = new Date(today.getFullYear(), today.getMonth() + 2, 0).toISOString().slice(0, 10);
        
        const dates = await getAvailableDates(selectedInstructor.id, startDate, endDate);
        setAvailableDates(dates);
      } catch (err) {
      }
    };

    fetchAvailableDates();
  }, [selectedInstructor]);

  const getAvailableDaysFromSchedule = () => {
    if (instructorAvailability.length === 0) return [];
    
    const daysMap = {
      'SUNDAY': 'Sunday',
      'MONDAY': 'Monday', 
      'TUESDAY': 'Tuesday',
      'WEDNESDAY': 'Wednesday',
      'THURSDAY': 'Thursday'
    };
    
    const uniqueDays = [...new Set(instructorAvailability.map(a => daysMap[a.dayOfWeek]))];
    return uniqueDays;
  };

  const formatDaysList = (days) => {
    if (days.length === 0) return '';
    if (days.length === 1) return days[0] + 's';
    if (days.length === 2) return days[0] + 's and ' + days[1] + 's';
    
    const lastDay = days[days.length - 1];
    const otherDays = days.slice(0, -1);
    return otherDays.join('s, ') + 's, and ' + lastDay + 's';
  };

  useEffect(() => {
    const fetchSlots = async () => {
      if (!selectedInstructor || !selectedDate) return;
      
      setLoadingSlots(true);
      setError(null);
      setSelectedSlot(null);
      
      try {
        const slots = await getAvailableSlots(selectedInstructor.id, selectedDate);
        setAvailableSlots(slots);
      } catch (err) {
        setError('Failed loading availability...');
        setAvailableSlots([]);
      } finally {
        setLoadingSlots(false);
      }
    };

    fetchSlots();
  }, [selectedInstructor, selectedDate]);

  const handleSelection = (inst) => {
    setSelectedInstructor(inst);
    setSelectedSlot(null);
    setAvailableSlots([]);
  };

 

  const handleSlotClick = (slot) => {
    setSelectedSlot(slot);
  };

  const handleConfirm = async () => {
    if (!selectedSlot) {
      setError('Please select a time slot');
      return;
    }

    if (!studentId) {
      setError('Please login first');
      return;
    }

    try {
      await createBooking(
        selectedInstructor.id,
        studentId,
        selectedSlot.startTime  
      );
      
      // success notification
      showSuccess('Booking successful!');
      
      // Reset state
      setSelectedInstructor(null);
      setAvailableSlots([]);
      setSelectedSlot(null);
      setAvailableDates([]);
      setInstructorAvailability([]);
      
      // Go back
      if (onBack) {
        onBack();
      }
    } catch (err) {
      const errorMsg = err?.response?.data?.message || err?.message || 'Failed to book appointment';
      showError('Booking failed: ' + errorMsg);
    }
  };

  const handleBackToInstructors = () => {
    setSelectedInstructor(null);
    setAvailableSlots([]);
    setSelectedSlot(null);
    setAvailableDates([]);
    setInstructorAvailability([]);
    setError(null);
  };

 
  const getMinDate = () => {
    return new Date().toISOString().slice(0, 10);
  };

  // Get maximum date 
  const getMaxDate = () => {
    const maxDate = new Date();
    maxDate.setDate(maxDate.getDate() + 30);
    return maxDate.toISOString().slice(0, 10);
  };

  if (loadingInstructors) {
    return (
      <div className="loading-container">
        <div className="loading-content">
          <div className="loading-spinner"></div>
          <p className="loading-text">Loading Instructors...</p>
        </div>
      </div>
    );
  }

  if (error && !selectedInstructor) {
    return (
      <div className="error-container">
        <div className="error-box">
          <p className="error-text">Error: {error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="book-appointment-container">
      <div className="book-appointment-wrapper">
        {!selectedInstructor ? (
          <>
            {/* Header for Instructors List */}
            <div className="instructors-header">
              <h2 className="instructors-title">Instructors List</h2>
              <p className="instructors-subtitle">Select an instructor to book an appointment</p>
            </div>

            {/* Instructors Grid */}
            <div className="instructors-grid">
              {instructors.map((doctor) => (
                <div key={doctor.id} className="doctor-card">
                  <div className="doctor-icon-container">
                    <div className="doctor-icon-wrapper">
                      <PersonIcon />
                    </div>
                  </div>
                  <h3 className="doctor-name">{doctor.name}</h3>
                  <p className="doctor-email">{doctor.email}</p>
                  {doctor.specialty && (
                    <p className="doctor-specialty">{doctor.specialty}</p>
                  )}
                  <button onClick={() => handleSelection(doctor)} className="book-button">
                    Book
                  </button>
                </div>
              ))}
            </div>
          </>
        ) : (
          <>
            {/* Back Button */}
            <button onClick={handleBackToInstructors} className="back-button-top">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
              Back to Instructors
            </button>

            {/* Booking View | Doctor Info + Time Slots */}
            <div className="booking-view-container">
              <div className="booking-view-layout">
                {/* Left Side | Doctor Info */}
                <div className="doctor-info-section">
                  <div className="doctor-icon-large-wrapper">
                    <PersonIcon className="doctor-icon-large" />
                  </div>
                  <h2 className="doctor-info-name">{selectedInstructor.name}</h2>
                  <p className="doctor-info-email">{selectedInstructor.email}</p>
                  {selectedInstructor.specialty && (
                    <p className="doctor-info-specialty">{selectedInstructor.specialty}</p>
                  )}
                </div>

                {/* Right Side | Date Selector & Time Slots */}
                <div className="slots-section">
                  {/* Date Selector */}
                  <div className="date-selector">
                    <label className="date-selector-label">Select Date:</label>
                    <input
                      type="date"
                      value={selectedDate}
                      onChange={(e) => setSelectedDate(e.target.value)}
                      min={getMinDate()}
                      max={getMaxDate()}
                      className="date-input"
                    />
                    <p className="date-hint">
                      {selectedDate && new Date(selectedDate + 'T00:00:00').toLocaleDateString('en-US', {
                        weekday: 'long',
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric'
                      })}
                    </p>
                    {instructorAvailability.length > 0 && (
                      <div className="available-dates-hint">
                        <i className="bi bi-calendar-check"></i> 
                        Instructor Available on {formatDaysList(getAvailableDaysFromSchedule())} .
                      </div>
                    )}
                  </div>

                  <h3 className="slots-header">Available Slots:</h3>

                  {loadingSlots ? (
                    <div className="slots-loading">
                      <div className="slots-loading-spinner"></div>
                      <p className="slots-loading-text">Loading slots...</p>
                    </div>
                  ) : error ? (
                    <div className="slots-error">
                      <p className="slots-error-text">{error}</p>
                    </div>
                  ) : availableSlots.length === 0 ? (
                    <div className="no-slots-message">
                      <p className="no-slots-text">No slots available for this date</p>
                      <p className="no-slots-hint">Try selecting a different date</p>
                    </div>
                  ) : (
                    <>
                      {/* Time Slots Grid */}
                      <div className="slots-grid">
                        {availableSlots.map((slot, index) => (
                          <button
                            key={`${slot.startTime}-${index}`}
                            onClick={() => handleSlotClick(slot)}
                            className={`slot-button ${selectedSlot?.startTime === slot.startTime ? 'selected' : ''}`}
                          >
                            {formatTimeSlot(slot.startTime)}
                          </button>
                        ))}
                      </div>

                      {/* Selected slot info */}
                      {selectedSlot && (
                        <div className="selected-slot-info">
                          <p>
                            Selected: {formatTimeSlot(selectedSlot.startTime)} - {formatTimeSlot(selectedSlot.endTime)}
                          </p>
                        </div>
                      )}

                      {/* Confirm Button */}
                      <button
                        onClick={handleConfirm}
                        disabled={!selectedSlot}
                        className="confirm-button"
                      >
                        Confirm Booking
                      </button>
                    </>
                  )}
                </div>
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default BookAppointment;