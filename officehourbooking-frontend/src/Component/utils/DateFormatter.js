export const formatDateTime = (dateTimeString) => {
  const date = new Date(dateTimeString);
  
  const dateStr = date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric'
  });

  const timeStr = date.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });

  return { date: dateStr, time: timeStr };
};


export const formatTimeSlot = (dateTimeString) => {
  const date = new Date(dateTimeString);
  return date.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });
};

export const formatDayTime = (dayOfWeek, startTime, endTime) => {
  const formatTime = (time) => {
    let hours, minutes;
    
    if (Array.isArray(time)) {
      [hours, minutes] = time;
    } else if (typeof time === 'string') {
      [hours, minutes] = time.split(':').map(Number);
    } else {
      return 'N/A';
    }

    const period = hours >= 12 ? 'PM' : 'AM';
    const displayHours = hours > 12 ? hours - 12 : hours === 0 ? 12 : hours;
    return `${displayHours}:${minutes.toString().padStart(2, '0')} ${period}`;
  };

  return `${dayOfWeek} ${formatTime(startTime)} - ${formatTime(endTime)}`;
};


export const formatEndTime = (dateTimeString) => {
  return new Date(dateTimeString).toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });
};