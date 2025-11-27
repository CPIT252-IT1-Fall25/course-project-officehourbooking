/*
 all availability-related constant values
 */

 
// Slot Duration Constants
export const DEFAULT_SLOT_MINUTES = 15;


export const SLOT_DURATIONS = {
  SHORT: 10,
  STANDARD: 15,
  MEDIUM: 20,
  LONG: 30
};

// Days of the Week Configuration
export const DAYS_OF_WEEK = [
  { value: 'SUNDAY', label: 'Sun' },
  { value: 'MONDAY', label: 'Mon' },
  { value: 'TUESDAY', label: 'Tue' },
  { value: 'WEDNESDAY', label: 'Wed' },
  { value: 'THURSDAY', label: 'Thu' }
];

// Time Slot Options for Selection
export const SLOT_TIME_OPTIONS = [
  { value: SLOT_DURATIONS.SHORT, label: '10 min' },
  { value: SLOT_DURATIONS.STANDARD, label: '15 min' },
  { value: SLOT_DURATIONS.MEDIUM, label: '20 min' },
  { value: SLOT_DURATIONS.LONG, label: '30 min' }
];

// Default Form Values
export const DEFAULT_START_TIME = '10:00';
export const DEFAULT_END_TIME = '12:00';

export const MINUTES_PER_HOUR = 60;