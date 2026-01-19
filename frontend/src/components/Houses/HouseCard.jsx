import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useLanguage } from '../../context/LanguageContext';
import './Houses.css';

const HouseCard = ({ house, onUpdate }) => {
  const navigate = useNavigate();
  const { t } = useLanguage();
  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  const getImageSrc = (imageData) => {
    if (!imageData) return null;
    // Handle base64 encoded image
    if (typeof imageData === 'string' && imageData.startsWith('data:')) {
      return imageData;
    }
    // Handle byte array
    return `data:image/jpeg;base64,${imageData}`;
  };

  const imageSrc = getImageSrc(house.image);

  return (
    <div className="house-card glass-card">
      {imageSrc && (
        <div className="house-image-container">
          <img src={imageSrc} alt={house.name} className="house-image" />
        </div>
      )}
      {!imageSrc && (
        <div className="house-image-placeholder">
          <svg
            width="64"
            height="64"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
            <polyline points="9 22 9 12 15 12 15 22" />
          </svg>
        </div>
      )}

      <div className="house-content">
        <h3 className="house-name">{house.name}</h3>
        <p className="house-address">{house.address}</p>
        <p className="house-description">{house.description}</p>

        <div className="house-details">
          <div className="house-detail-item">
            <span className="detail-label">{t('houses.totalPrice')}</span>
            <span className="detail-value">{formatPrice(house.price)}</span>
          </div>
          <div className="house-detail-item">
            <span className="detail-label">{t('houses.available')}</span>
            <span className="detail-value">
              {formatDate(house.availableFrom)} - {formatDate(house.availableTo)}
            </span>
          </div>
        </div>

        <button
          className="glass-button primary house-button"
          onClick={() => navigate(`/houses/${house.id}`)}
        >
          {t('houses.viewDetails')}
        </button>
      </div>
    </div>
  );
};

export default HouseCard;
