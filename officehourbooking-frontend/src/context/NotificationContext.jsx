import React, { createContext, useContext, useState, useCallback } from 'react';
import Notification from '../Component/utils/Notification';

const NotificationContext = createContext(null);

export const NotificationProvider = ({ children }) => {
  const [notifications, setNotifications] = useState([]);

  const addNotification = useCallback((message, type = 'info', duration = 3000) => {
    const id = Date.now();
    setNotifications(prev => [...prev, { id, message, type, duration }]);
  }, []);

  const removeNotification = useCallback((id) => {
    setNotifications(prev => prev.filter(notif => notif.id !== id));
  }, []);

  const showSuccess = useCallback((message) => {
    addNotification(message, 'success');
  }, [addNotification]);

  const showError = useCallback((message) => {
    addNotification(message, 'error', 5000);
  }, [addNotification]);

  const showWarning = useCallback((message) => {
    addNotification(message, 'warning');
  }, [addNotification]);

  const showInfo = useCallback((message) => {
    addNotification(message, 'info');
  }, [addNotification]);

  return (
    <NotificationContext.Provider value={{ showSuccess, showError, showWarning, showInfo }}>
      {children}
      <div style={{ position: 'fixed', top: 0, right: 0, zIndex: 10000 }}>
        {notifications.map((notif, index) => (
          <div key={notif.id} style={{ marginTop: index > 0 ? '10px' : '0' }}>
            <Notification
              message={notif.message}
              type={notif.type}
              duration={notif.duration}
              onClose={() => removeNotification(notif.id)}
            />
          </div>
        ))}
      </div>
    </NotificationContext.Provider>
  );
};

export const useNotification = () => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error('useNotification must be used within a NotificationProvider');
  }
  return context;
};