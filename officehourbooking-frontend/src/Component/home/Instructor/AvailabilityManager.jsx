import React from 'react';
import {
  DAYS_OF_WEEK,
  SLOT_TIME_OPTIONS
} from '../../home/availability';

const AvailabilityManager = ({
  availability,
  loading,
  showAddForm,
  setShowAddForm,
  formData,
  formError,
  formLoading,
  onNavigate,
  onFormChange,
  onAddAvailability,
  onCancelForm,
  onDeleteAvailability,
  calculateSlots,
  formatDayTime
}) => {
  return (
    <div className="view-container">
      <button onClick={() => onNavigate('dashboard')} className="back-button-top">
        <i className="bi bi-arrow-left"></i> Back
      </button>

      <div className="welcome-section">
        <h1>Office Hours</h1>
        <p>Manage your availability schedule</p>
      </div>

      {/* Add availability */}
      <div className="add-availability-section">
        {!showAddForm ? (
          <button className="btn btn-primary btn-add" onClick={() => setShowAddForm(true)}>
            <i className="bi bi-plus-circle"></i> Add Availability
          </button>
        ) : (
          <div className="add-availability-form">
            <div className="form-header">
              <h3>
                <i className="bi bi-calendar-plus"></i> Add Office Hours
              </h3>
              <p className="form-description">Set up your availability</p>
            </div>

            <div className="form-body">
              {formError && (
                <div className="form-error">
                  <i className="bi bi-exclamation-circle"></i> {formError}
                </div>
              )}

              {/* Day Selection */}
              <div className="form-section">
                <label className="section-label">
                  <i className="bi bi-calendar-week"></i> Select Day
                </label>
                <div className="day-selector">
                  {DAYS_OF_WEEK.map((day) => (
                    <button
                      key={day.value}
                      type="button"
                      className={`day-button ${formData.dayOfWeek === day.value ? 'selected' : ''}`}
                      onClick={() => onFormChange('dayOfWeek', day.value)}
                    >
                      {day.label}
                    </button>
                  ))}
                </div>
              </div>

              {/* Time Selection */}
              <div className="form-section">
                <label className="section-label">
                  <i className="bi bi-clock"></i> Time Range
                </label>
                <div className="time-range-container">
                  <div className="time-input-group">
                    <span className="time-label">From</span>
                    <input
                      type="time"
                      className="time-input"
                      value={formData.startTime}
                      onChange={(e) => onFormChange('startTime', e.target.value)}
                    />
                  </div>
                  <span className="time-separator">→</span>
                  <div className="time-input-group">
                    <span className="time-label">To</span>
                    <input
                      type="time"
                      className="time-input"
                      value={formData.endTime}
                      onChange={(e) => onFormChange('endTime', e.target.value)}
                    />
                  </div>
                </div>
              </div>

              {/* Slot Duration */}
              <div className="form-section">
                <label className="section-label">
                  <i className="bi bi-hourglass-split"></i> Slot Duration
                </label>
                <div className="duration-selector">
                  {SLOT_TIME_OPTIONS.map((duration) => (
                    <button
                      key={duration.value}
                      type="button"
                      className={`duration-button ${
                        formData.slotMinutes === duration.value ? 'selected' : ''
                      }`}
                      onClick={() => onFormChange('slotMinutes', duration.value)}
                    >
                      <i className="bi bi-stopwatch"></i>
                      {duration.label}
                    </button>
                  ))}
                </div>
              </div>

              {/* Slots Preview */}
              {calculateSlots() > 0 && (
                <div className="slots-preview">
                  <i className="bi bi-info-circle"></i>
                  This will create <strong>{calculateSlots()}</strong> appointment slot
                  {calculateSlots() !== 1 ? 's' : ''}
                </div>
              )}
            </div>

            <div className="form-footer">
              <button className="btn btn-secondary" onClick={onCancelForm} disabled={formLoading}>
                Cancel
              </button>
              <button
                className="btn btn-primary btn-save"
                onClick={onAddAvailability}
                disabled={formLoading || !formData.dayOfWeek}
              >
                {formLoading ? (
                  <>
                    <i className="bi bi-hourglass-split"></i> Saving...
                  </>
                ) : (
                  <>
                    <i className="bi bi-check-circle"></i> Save Availability
                  </>
                )}
              </button>
            </div>
          </div>
        )}
      </div>

      {loading ? (
        <div className="loading-message">Loading availability...</div>
      ) : (
        <div className="availability-list">
          {availability.length === 0 && !showAddForm ? (
            <div className="no-availability">
              <i className="bi bi-clock-history empty-icon"></i>
              <p>No office hours set yet.</p>
              <p className="hint">Click "Add Availability" to set up your schedule</p>
            </div>
          ) : (
            availability.map((slot) => (
              <div key={slot.id} className="availability-card">
                <div className="availability-info">
                  <i className="bi bi-calendar-week"></i>
                  <div>
                    <h3>{formatDayTime(slot.dayOfWeek, slot.startTime, slot.endTime)}</h3>
                    <p>Slot duration: {slot.slotMinutes} minutes</p>
                  </div>
                </div>
                <button
                  className="btn btn-danger btn-small"
                  onClick={() => onDeleteAvailability(slot.id)}
                >
                  <i className="bi bi-trash"></i> Delete
                </button>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default AvailabilityManager;