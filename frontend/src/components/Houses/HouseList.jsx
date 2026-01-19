import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { houseAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { useLanguage } from '../../context/LanguageContext';
import LanguageSwitcher from '../LanguageSwitcher/LanguageSwitcher';
import HouseCard from './HouseCard';
import './Houses.css';

const HouseList = () => {
  const [houses, setHouses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { logout } = useAuth();
  const { t } = useLanguage();
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
          <h1 className="header-title">{t('houses.title')}</h1>
          <div className="header-actions">
            <LanguageSwitcher />
            <button
              className="glass-button primary"
              onClick={handleAddHouse}
            >
              + {t('houses.addHouse')}
            </button>
            <button
              className="glass-button"
              onClick={handleLogout}
            >
              {t('houses.logout')}
            </button>
          </div>
        </div>
      </header>

      <div className="container">
        {loading && (
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p className="loading-text">{t('houses.loading')}</p>
          </div>
        )}

        {error && (
          <div className="error-container glass-card">
            <p className="error-text">{error}</p>
            <button className="glass-button" onClick={loadHouses}>
              {t('houses.retry')}
            </button>
          </div>
        )}

        {!loading && !error && houses.length === 0 && (
          <div className="empty-state glass-card fade-in">
            <h2>{t('houses.noHouses')}</h2>
            <p>{t('houses.noHousesSubtitle')}</p>
            <button className="glass-button primary mt-3" onClick={handleAddHouse}>
              {t('houses.addFirstHouse')}
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
