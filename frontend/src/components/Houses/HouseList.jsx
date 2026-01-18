import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { houseAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import HouseCard from './HouseCard';
import './Houses.css';

const HouseList = () => {
  const [houses, setHouses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    loadHouses();
  }, []);

  const loadHouses = async () => {
    try {
      setLoading(true);
      const data = await houseAPI.getAll();
      setHouses(data);
      setError('');
    } catch (err) {
      if (err.response?.status === 401) {
        setError('Session expired. Please log in again.');
        setTimeout(() => {
          logout();
          navigate('/login');
        }, 2000);
      } else {
        setError('Failed to load houses. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleAddHouse = () => {
    navigate('/houses/new');
  };

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <div className="houses-container">
      <header className="houses-header glass">
        <div className="header-content">
          <h1 className="header-title">House Reservations</h1>
          <div className="header-actions">
            <button
              className="glass-button primary"
              onClick={handleAddHouse}
            >
              + Add House
            </button>
            <button
              className="glass-button"
              onClick={handleLogout}
            >
              Logout
            </button>
          </div>
        </div>
      </header>

      <div className="container">
        {loading && (
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p className="loading-text">Loading houses...</p>
          </div>
        )}

        {error && (
          <div className="error-container glass-card">
            <p className="error-text">{error}</p>
            <button className="glass-button" onClick={loadHouses}>
              Retry
            </button>
          </div>
        )}

        {!loading && !error && houses.length === 0 && (
          <div className="empty-state glass-card fade-in">
            <h2>No houses available</h2>
            <p>Be the first to add a house to the system!</p>
            <button className="glass-button primary mt-3" onClick={handleAddHouse}>
              Add Your First House
            </button>
          </div>
        )}

        {!loading && !error && houses.length > 0 && (
          <div className="houses-grid fade-in">
            {houses.map((house) => (
              <HouseCard key={house.id} house={house} onUpdate={loadHouses} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default HouseList;
