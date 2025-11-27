export const UserRole = {
  STUDENT: 'STUDENT',
  DOCTOR: 'DOCTOR',
  INSTRUCTOR: 'DOCTOR' 
};

export const isStudent = (role) => role === UserRole.STUDENT;

export const isDoctor = (role) => role === UserRole.DOCTOR;

export const getRoleDisplayText = (role) => {
  switch (role) {
    case UserRole.STUDENT:
      return 'Student';
    case UserRole.DOCTOR:
      return 'Instructor';
    default:
      return role;
  }
};