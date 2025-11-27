import React, { useEffect } from 'react';
import './Notification.css';

const Notification = ({ message, type = 'info', onClose, duration = 3000 }) => {
  useEffect(() => {
    if (duration) {
      const timer = setTimeout(() => {
        onClose();
      }, duration);
      return () => clearTimeout(timer);
    }
  }, [duration, onClose]);

  const getIcon = () => {
    switch (type) {
      case 'success':
        return 'bi-check-circle-fill';
      case 'error':
        return 'bi-x-circle-fill';
      case 'warning':
        return 'bi-exclamation-triangle-fill';
      default:
        return 'bi-info-circle-fill';
    }
  };

  return (
    <div className={`Notification Notification-${type}`}>
      <i className={`bi ${getIcon()}`}></i>
      <span className="Notification-message">{message}</span>
      <button className="Notification-close" onClick={onClose}>
        <i className="bi bi-x"></i>
      </button>
    </div>
  );
};

export default Notification;