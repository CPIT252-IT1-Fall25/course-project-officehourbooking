import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Auth.css';

const Login = () => {
  const navigate = useNavigate();
  const { login, checkEmail} = useAuth();
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [validationErrors, setValidationErrors] = useState({}); 

  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });

  const handleChange = (e) => {
    const {name, value} = e.target;
    
    setFormData({
      ...formData,
      [name]: value
    });


    if (validationErrors[name]){
      setValidationErrors({
        ...validationErrors,
        [name] : ''
      });
    }


    if (error) setError('');
  };

    const validateEmail = (email) =>{
      const validDomains = ['@stu.kau.edu.sa', '@kau.edu.sa'];
      const isValid = validDomains.some(domain => email.toLowerCase().endsWith(domain));

      if(!isValid) {
        return 'Please use a valid KAU email';
      }
      return '';
    };


  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setValidationErrors({});
    setLoading(true);

    try {

      const emailErr = validateEmail(formData.email);
      if (emailErr) {
        setValidationErrors({email: emailErr});
        setLoading(false);
        return;
      }


      const emailDomain = await checkEmail(formData.email);

      if (emailDomain === `INVALID_DOMAIN`){
        setValidationErrors({
          email: `Please use @stu.kau.edu.sa or @kau.edu.sa`
        });
        setLoading(false);
        return;
      }


      const response = await login(formData);

      if(response.role === 'STUDENT'){
        navigate('/StudentHome')
      }else {
        navigate('/InstructorHome')
      }
      
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }

  };


  return (
  
    <div className="auth-wrapper">
  {/* Left Side */}
  <div className="auth-left">
    <div className="auth-info">
      <h1>Office Hours Booking</h1>
      <p>Schedule and manage your meetings with Instructors.</p>
    </div>
  </div>

  {/* Right Side (Login Form) */}
  <div className="auth-container">
    <div className="auth-card">
      <h2>Login</h2>
      <p className="auth-subtitle">Welcome back!</p>
      <hr></hr> 
      {error && <div className="error-message">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            autoCapitalize='email'
            autoComplete='email'
          />
              {validationErrors.email && (
                <span className="error-text">{validationErrors.email}</span>
              )}
             
        </div>

        <div className="form-group">
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            autoComplete='current-password'
          />
        </div>

        <button type="submit" className="auth-button" disabled={loading}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>

      <p className="auth-link">
        Don't have an account? <Link to="/signup">Sign up here</Link>
      </p>
    </div>
  </div>
</div>
  );
};

export default Login;