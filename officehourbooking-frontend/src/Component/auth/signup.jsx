import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './SignUp.css';

const SignUp = () => {
  const navigate = useNavigate();
  const { signup } = useAuth();
  const [currentStep, setCurrentStep] = useState(0);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const [formData, setFormData] = useState({
    role: '',
    name: '',
    email: '',
    studentId: '',
    specialization: '',
   
    password: '',
    confirmPassword: ''
  });

  const [validationErrors, setValidationErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData({
      ...formData,
      [name]: value
    });

    if (validationErrors[name]) {
      setValidationErrors({
        ...validationErrors,
        [name]: ''
      });
    }
  };

  const validate_RoleSelection = () => {
    const errors = {};
    if (!formData.role) {
      errors.role = 'Please select your role';
    }
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const validateStep1 = () => {
    const errors = {};
    
    if (!formData.name.trim()) {
      errors.name = 'Full name is required';
    } else if (formData.name.trim().length < 3) {
      errors.name = 'Name must be at least 3 characters';
    }

    if (!formData.email.trim()) {
      errors.email = 'Email is required';
    } else {
      const email = formData.email.toLowerCase().trim();
      
      if (formData.role === 'INSTRUCTOR') {
        if (!/@kau\.edu\.sa$/.test(email)) {
          errors.email = 'Instructors must use @kau.edu.sa email address';
        }
      } else if (formData.role === 'STUDENT') {
        if (!/@stu\.kau\.edu\.sa$/.test(email)) {
          errors.email = 'Students must use @stu.kau.edu.sa email address';
        }
      } else {
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
          errors.email = 'Please enter a valid email address';
        }
      }
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const validateStep2 = () => {
    const errors = {};

    if (formData.role === 'STUDENT') {
      if (!formData.studentId.trim()) {
        errors.studentId = 'Student ID is required';
      }
    } 

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const validateStep3 = () => {
    const errors = {};

    if (!formData.password) {
      errors.password = 'Password is required';
    } else if (formData.password.length < 6) {
      errors.password = 'Password must be at least 6 characters';
    }

    if (!formData.confirmPassword) {
      errors.confirmPassword = 'Please confirm your password';
    } else if (formData.password !== formData.confirmPassword) {
      errors.confirmPassword = 'Passwords do not match';
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleNext = () => {
    setError('');
    let isValid = false;

    if (currentStep === 0) {
      isValid = validate_RoleSelection();
    } else if (currentStep === 1) {
      isValid = validateStep1();
    } else if (currentStep === 2) {
      isValid = validateStep2();
    }

    if (isValid) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handleBack = () => {
    setError('');
    setValidationErrors({});
    setCurrentStep(currentStep - 1);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!validateStep3()) {
      return;
    }

    setLoading(true);

    try {
      const signupData = {
        name: formData.name,
        email: formData.email,
        password: formData.password,
        role: formData.role 
      };

      if (formData.role === 'STUDENT') {
        signupData.universityId = formData.studentId;
      } else {
        signupData.specialty = formData.specialization;
      }

      console.log('Payload being sent to backend:', signupData);

      const response = await signup(signupData);
      
      if (response.role === 'STUDENT') {
        navigate('/StudentHome');
      } else {
        navigate('/InstructorHome');
      }
    } catch (err) {
      console.error('Signup error:', err.response?.data || err.message);
      setError(err.response?.data?.message || 'Sign up failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const roleSelection = () => (
    <div className="step-content">
      <p className="step-description">What's your role?</p>
     
      <div className="role-selector">
        <label className={formData.role === 'STUDENT' ? 'active' : ''}>
          <input
            type="radio"
            name="role"
            value="STUDENT"
            checked={formData.role === 'STUDENT'}
            onChange={handleChange}
          />
          <span className="role-icon">👨‍🎓</span>
          <span>Student</span>
        </label>
        <label className={formData.role === 'INSTRUCTOR' ? 'active' : ''}>
          <input
            type="radio"
            name="role"
            value="INSTRUCTOR"
            checked={formData.role === 'INSTRUCTOR'}
            onChange={handleChange}
          />
          <span className="role-icon">👨‍🏫</span>
          <span>Instructor</span>
        </label>
      </div>
         
      {validationErrors.role && (
        <span className="error-text" style={{ display: 'block', marginTop: '10px', color: 'red' }}>
          {validationErrors.role}
        </span>
      )}
      
      <button type="button" onClick={handleNext} className="next-button">
        Next →
      </button>
    </div>
  );

  const displayStep1 = () => (
    <div className="step-content">
      <div className="form-group">
        <label>Full Name *</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
          placeholder="Enter your full name"
          className={validationErrors.name ? 'error' : ''}
        />
        {validationErrors.name && <span className="error-text">{validationErrors.name}</span>}
      </div>

      <div className="form-group">
        <label>Email Address *</label>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="your.email@example.com"
          className={validationErrors.email ? 'error' : ''}
        />
        {validationErrors.email && <span className="error-text">{validationErrors.email}</span>}
      </div>

      <div className="button-group">
        <button type="button" onClick={handleBack} className="back-button">
          ← Back
        </button>
        <button type="button" onClick={handleNext} className="next-button">
          Next →
        </button>
      </div>
    </div>
  );

  const displayStep2Student = () => (
    <div className="step-content">
      <h3>Academic Information</h3>
      <p className="step-description">Tell us about your studies</p>

      <div className="form-group">
        <label>Student ID *</label>
        <input
          type="text"
          name="studentId"
          value={formData.studentId}
          onChange={handleChange}
          placeholder="e.g., 2024001234"
          className={validationErrors.studentId ? 'error' : ''}
        />
        {validationErrors.studentId && <span className="error-text">{validationErrors.studentId}</span>}
      </div>


      <div className="button-group">
        <button type="button" onClick={handleBack} className="back-button">
          ← Back
        </button>
        <button type="button" onClick={handleNext} className="next-button">
          Next →
        </button>
      </div>
    </div>
  );

  const displayStep2Instructor = () => (
    <div className="step-content">
      <h3>Professional Information</h3>
      <p className="step-description">Tell us about your role</p>

      <div className="form-group">
        <label>Specialization (Optional)</label>
        <input
          type="text"
          name="specialization"
          value={formData.specialization}
          onChange={handleChange}
          placeholder="e.g., Artificial Intelligence"
        />
      </div>

      <div className="button-group">
        <button type="button" onClick={handleBack} className="back-button">
          ← Back
        </button>
        <button type="button" onClick={handleNext} className="next-button">
          Next →
        </button>
      </div>
    </div>
  );

  const displayStep3 = () => (
    <div className="step-content">
      <h3>Create Password</h3>
      <p className="step-description">Secure your account</p>

      <div className="form-group">
        <label>Password *</label>
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          placeholder="At least 6 characters"
          autoComplete='new-password'
          className={validationErrors.password ? 'error' : ''}
        />
        {validationErrors.password && <span className="error-text">{validationErrors.password}</span>}
      </div>

      <div className="form-group">
        <label>Confirm Password *</label>
        <input
          type="password"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
          placeholder="Re-enter your password"
          autoComplete='new-password'
          className={validationErrors.confirmPassword ? 'error' : ''}
        />
        {validationErrors.confirmPassword && <span className="error-text">{validationErrors.confirmPassword}</span>}
      </div>

      <div className="button-group">
        <button type="button" onClick={handleBack} className="back-button">
          ← Back
        </button>
        <button type="submit" className="submit-button" disabled={loading}>
          {loading ? 'Creating Account...' : 'Create Account'}
        </button>
      </div>
    </div>
  );

  return (
    <div className="multistep-container">
      <div className="multistep-card">
        <div className="card-header">
          <h2>Sign Up</h2>
          {formData.role && (
            <p className="role-text">as {formData.role === 'STUDENT' ? 'Student' : 'Instructor'}</p>
          )}
          <hr />
        </div>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          {currentStep === 0 && roleSelection()}
          {currentStep === 1 && displayStep1()}
          {currentStep === 2 && (formData.role === 'STUDENT' ? displayStep2Student() : displayStep2Instructor())}
          {currentStep === 3 && displayStep3()}
        </form>

        <p className="auth-link">
          Already have an account? <Link to="/login">Login here</Link>
        </p>
      </div>
    </div>
  );
};

export default SignUp;