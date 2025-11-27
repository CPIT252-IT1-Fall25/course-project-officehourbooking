
export const BookingStatus = {
  CONFIRMED: 'CONFIRMED',
  CANCELLED: 'CANCELLED'
};


export const isConfirmed = (status) => status === BookingStatus.CONFIRMED;


export const isCancelled = (status) => status === BookingStatus.CANCELLED;


export const getStatusDisplayText = (status) => {
  switch (status) {
    case BookingStatus.CONFIRMED:
      return 'Confirmed';
    case BookingStatus.CANCELLED:
      return 'Canceled';
    default:
      return status;
  }
};