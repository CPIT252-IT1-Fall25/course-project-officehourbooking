import {useAuth} from '../../context/AuthContext';
import { useNavigate } from "react-router";
import './home.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

const InstructorHome = () => {
    const {user, logout} = useAuth();
    const nav =  useNavigate();

    const handleLogout = () => {
        logout();
        nav('/login');
    };

    return(
        <div className="home-container">
            <nav className="navbar">
                <div className="navbar-logo">
                    <h2>Office hours Booking</h2>

                </div>
                <div className="navbar-menu">
                    <span className="user-name">Welcome, {user?.name}</span>
                    <button  onClick={handleLogout} 
                    type="button" 
                    className="btn btn-danger">Logout</button>
                </div>
            </nav>

            <div className="home-content">
                <div className="welcom-section">
                    <h1>Instructor Dashboard</h1>
                    <p> Schedule appointments with your Students</p>
                </div>

                <div className="dashboard">
                    <div className="dashboard-card">
                        <i className="bi bi-calendar-date"></i>
                        <h3>Appointment</h3>
                        <p>Working on that... </p>

                    </div>

 
        </div>
    </div>
    </div>
    );
};

export default InstructorHome