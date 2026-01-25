import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useLanguage } from '../../context/LanguageContext';
import { useAuth } from '../../context/AuthContext';
import { houseAPI, reservationAPI } from '../../services/api';
import './HouseDetail.css';

const HouseDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { t } = useLanguage();
  const { user } = useAuth();

  const [house, setHouse] = useState(null);
  const [houseDays, setHouseDays] = useState([]);
  const [daysMap, setDaysMap] = useState(new Map());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [selectedDays, setSelectedDays] = useState([]);
  const [reservationLoading, setReservationLoading] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });
  const [dailyPrice, setDailyPrice] = useState(0);
  const [paymentSummary, setPaymentSummary] = useState({ byUser: {}, remaining: 0 });

  useEffect(() => {
    loadHouseData();
  }, [id]);

  const loadHouseData = async () => {
    try {
      setLoading(true);
      setError('');

      const [houseData, houseDaysData] = await Promise.all([
        houseAPI.getById(id),
        houseAPI.getHouseDays(id)
      ]);

      setHouse(houseData);
      setHouseDays(houseDaysData);

      // Build a map for quick lookup: date string -> day object
      const map = new Map();
      houseDaysData.forEach(day => {
        map.set(day.date, day);
      });
      setDaysMap(map);

      // Calculate daily price
      const totalDays = houseDaysData.length;
      const pricePerDay = totalDays > 0 ? houseData.price / totalDays : 0;
      setDailyPrice(pricePerDay);

      // Calculate payment summary by user
      const userPayments = {};
      let reservedCount = 0;
      houseDaysData.forEach(day => {
        if (day.reservedByUserId !== null && day.reservedByUsername) {
          const username = day.reservedByUsername;
          if (!userPayments[username]) {
            userPayments[username] = { days: 0, baseAmount: 0, additionalAmount: 0, totalAmount: 0 };
          }
          userPayments[username].days += 1;
          userPayments[username].baseAmount += pricePerDay;
          reservedCount += 1;
        }
      });

      // Calculate unreserved days and split among users with reservations
      const remainingDays = totalDays - reservedCount;
      const remainingAmount = remainingDays * pricePerDay;
      const userCount = Object.keys(userPayments).length;
      const additionalPerUser = userCount > 0 ? remainingAmount / userCount : 0;

      // Add additional amount to each user
      Object.keys(userPayments).forEach(username => {
        userPayments[username].additionalAmount = additionalPerUser;
        userPayments[username].totalAmount = userPayments[username].baseAmount + additionalPerUser;
      });

      setPaymentSummary({
        byUser: userPayments,
        remaining: remainingAmount,
        remainingDays,
        userCount
      });

    } catch (err) {
      console.error('Failed to load house data:', err);
      if (err.response?.status === 401) {
        setError('Session expired. Redirecting to login...');
        setTimeout(() => navigate('/login'), 2000);
      } else {
        setError(t('houseDetail.errorLoading'));
      }
    } finally {
      setLoading(false);
    }
  };

  const getDayStatus = (dateString) => {
    if (!house) return 'outside-range';

    const date = new Date(dateString);
    const availableFrom = new Date(house.availableFrom);
    const availableTo = new Date(house.availableTo);

    // Check if date is within range
    if (date < availableFrom || date > availableTo) {
      return 'outside-range';
    }

    const dayData = daysMap.get(dateString);
    if (!dayData) return 'available';

    if (dayData.reservedByUserId === null) {
      return 'available';
    } else if (dayData.reservedByUserId === user?.id) {
      return 'reserved-by-user';
    } else {
      return 'reserved-by-other';
    }
  };

  const handleDayClick = (dateString, status) => {
    if (status === 'outside-range' || status === 'reserved-by-other') {
      return;
    }

    setSelectedDays(prev => {
      if (prev.includes(dateString)) {
        return prev.filter(d => d !== dateString);
      } else {
        return [...prev, dateString];
      }
    });
  };

  const handleReserve = async () => {
    if (selectedDays.length === 0) return;

    setReservationLoading(true);
    setMessage({ type: '', text: '' });

    const failures = [];
    const successes = [];

    for (const dateString of selectedDays) {
      try {
        const dayData = daysMap.get(dateString);
        if (dayData && dayData.id) {
          await reservationAPI.create(dayData.id);
          successes.push(dateString);
        }
      } catch (err) {
        console.error(`Failed to reserve ${dateString}:`, err);
        failures.push(dateString);
      }
    }

    setReservationLoading(false);

    if (failures.length === 0) {
      setMessage({ type: 'success', text: t('houseDetail.reservationSuccess') });
    } else {
      setMessage({
        type: 'error',
        text: `${t('houseDetail.reservationError')}: ${failures.join(', ')}`
      });
    }

    // Reload data and clear selection
    await loadHouseData();
    setSelectedDays([]);

    // Clear message after 3 seconds
    setTimeout(() => setMessage({ type: '', text: '' }), 3000);
  };

  const handleCancel = async () => {
    if (selectedDays.length === 0) return;

    setReservationLoading(true);
    setMessage({ type: '', text: '' });

    const failures = [];
    const successes = [];

    for (const dateString of selectedDays) {
      try {
        const dayData = daysMap.get(dateString);
        if (dayData && dayData.id) {
          await reservationAPI.deleteByHouseDayId(dayData.id);
          successes.push(dateString);
        }
      } catch (err) {
        console.error(`Failed to cancel ${dateString}:`, err);
        failures.push(dateString);
      }
    }

    setReservationLoading(false);

    if (failures.length === 0) {
      setMessage({ type: 'success', text: t('houseDetail.cancellationSuccess') });
    } else {
      setMessage({
        type: 'error',
        text: `${t('houseDetail.cancellationError')}: ${failures.join(', ')}`
      });
    }

    // Reload data and clear selection
    await loadHouseData();
    setSelectedDays([]);

    // Clear message after 3 seconds
    setTimeout(() => setMessage({ type: '', text: '' }), 3000);
  };

  const changeMonth = (offset) => {
    const newDate = new Date(currentMonth);
    newDate.setMonth(newDate.getMonth() + offset);
    setCurrentMonth(newDate);
  };

  const renderCalendar = () => {
    const year = currentMonth.getFullYear();
    const month = currentMonth.getMonth();

    // Get first day of month and total days
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startDayOfWeek = firstDay.getDay(); // 0 = Sunday

    // Build array of day cells
    const dayCells = [];

    // Add empty cells for alignment
    for (let i = 0; i < startDayOfWeek; i++) {
      dayCells.push(<div key={`empty-${i}`} className="calendar-day empty"></div>);
    }

    // Add day cells
    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(year, month, day);
      const dateString = date.toISOString().split('T')[0]; // YYYY-MM-DD
      const status = getDayStatus(dateString);
      const isSelected = selectedDays.includes(dateString);
      const dayData = daysMap.get(dateString);

      const className = `calendar-day ${status} ${isSelected ? 'selected' : ''}`;

      const isInRange = status !== 'outside-range';

      dayCells.push(
        <div
          key={dateString}
          className={className}
          onClick={() => handleDayClick(dateString, status)}
          title={
            status === 'reserved-by-other' && dayData?.reservedByUsername
              ? `${t('houseDetail.reservedByUser')} ${dayData.reservedByUsername}`
              : ''
          }
        >
          <span className="day-number">{day}</span>
          {isInRange && dailyPrice > 0 && (
            <span className="day-price">{dailyPrice.toFixed(2)}€</span>
          )}
          {status === 'reserved-by-other' && dayData?.reservedByUsername && (
            <span className="reserved-by">{dayData.reservedByUsername}</span>
          )}
        </div>
      );
    }

    return dayCells;
  };

  const renderHouseInfo = () => {
    if (!house) return null;

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString();
    };

    return (
      <div className="house-info-card glass-panel">
        {house.photo && (
          <div className="house-image">
            <img src={`data:image/jpeg;base64,${house.photo}`} alt={house.name} />
          </div>
        )}
        <h2>{house.name}</h2>
        <p className="house-address">{house.address}</p>

        <div className="house-info-section">
          <h3>{t('houseDetail.pricePerPeriod')}</h3>
          <p className="house-price">{formatPrice(house.totalPrice)}</p>
        </div>

        <div className="house-info-section">
          <h3>{t('houseDetail.availabilityPeriod')}</h3>
          <p className="house-dates">
            {formatDate(house.availableFrom)} - {formatDate(house.availableTo)}
          </p>
        </div>

        {house.description && (
          <div className="house-info-section">
            <h3>{t('houseDetail.description')}</h3>
            <p className="house-description">{house.description}</p>
          </div>
        )}
      </div>
    );
  };

  const renderReservationControls = () => {
    if (selectedDays.length === 0) return null;

    // Determine if user is selecting available days or their own reservations
    const hasAvailableDays = selectedDays.some(dateString => {
      const status = getDayStatus(dateString);
      return status === 'available';
    });

    const hasUserReservations = selectedDays.some(dateString => {
      const status = getDayStatus(dateString);
      return status === 'reserved-by-user';
    });

    const selectedPrice = selectedDays.length * dailyPrice;

    return (
      <div className="reservation-controls">
        <div className="selection-info">
          <span className="selection-count">
            {selectedDays.length} {t('houseDetail.daysSelected')}
          </span>
          <span className="selection-price">
            {formatPrice(selectedPrice)}
          </span>
        </div>
        <div className="action-buttons">
          {hasAvailableDays && (
            <button
              className="glass-button primary"
              onClick={handleReserve}
              disabled={reservationLoading}
            >
              {reservationLoading ? t('houseDetail.reserving') : t('houseDetail.reserveSelected')}
            </button>
          )}
          {hasUserReservations && (
            <button
              className="glass-button danger"
              onClick={handleCancel}
              disabled={reservationLoading}
            >
              {reservationLoading ? t('houseDetail.cancelling') : t('houseDetail.cancelSelected')}
            </button>
          )}
        </div>
      </div>
    );
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'EUR',
      minimumFractionDigits: 2,
    }).format(price);
  };

  const renderPaymentSummary = () => {
    const users = Object.entries(paymentSummary.byUser);
    const hasUnreserved = paymentSummary.remainingDays > 0;

    return (
      <div className="payment-summary glass-panel">
        <h3>{t('houseDetail.paymentSummary') || 'Payment Summary'}</h3>

        <div className="payment-breakdown">
          {users.length > 0 ? (
            <div className="user-payments">
              {users.map(([username, data]) => (
                <div key={username} className="user-payment-card">
                  <div className="user-payment-header">
                    <span className="payment-user">{username}</span>
                    <span className="payment-total">{formatPrice(data.totalAmount)}</span>
                  </div>
                  <div className="user-payment-details">
                    <div className="payment-detail-row">
                      <span className="detail-label">{t('houseDetail.forReservedDays') || 'For reserved days'}</span>
                      <span className="detail-info">{data.days} {t('houseDetail.days') || 'days'}</span>
                      <span className="detail-amount">{formatPrice(data.baseAmount)}</span>
                    </div>
                    {hasUnreserved && data.additionalAmount > 0 && (
                      <div className="payment-detail-row additional">
                        <span className="detail-label">{t('houseDetail.forUnreservedDays') || 'For unreserved days'}</span>
                        <span className="detail-info">{t('houseDetail.splitBetween') || 'split between'} {paymentSummary.userCount}</span>
                        <span className="detail-amount">+{formatPrice(data.additionalAmount)}</span>
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="no-reservations">{t('houseDetail.noReservationsYet') || 'No reservations yet'}</p>
          )}

          {hasUnreserved && (
            <div className="remaining-payment">
              <h4>{t('houseDetail.unreservedDays') || 'Unreserved days'}</h4>
              <div className="payment-row remaining">
                <span className="payment-days">{paymentSummary.remainingDays} {t('houseDetail.days') || 'days'}</span>
                <span className="payment-amount">{formatPrice(paymentSummary.remaining)}</span>
              </div>
              {users.length > 0 && (
                <p className="split-info">
                  {t('houseDetail.splitInfo') || 'This amount is split equally between all users with reservations'}
                </p>
              )}
            </div>
          )}

          <div className="total-row">
            <span className="total-label">{t('houseDetail.totalPrice') || 'Total Price'}</span>
            <span className="total-amount">{formatPrice(house?.price || 0)}</span>
          </div>
        </div>
      </div>
    );
  };

  const getMonthName = (month) => {
    const monthNames = [
      t('houseDetail.january'),
      t('houseDetail.february'),
      t('houseDetail.march'),
      t('houseDetail.april'),
      t('houseDetail.may'),
      t('houseDetail.june'),
      t('houseDetail.july'),
      t('houseDetail.august'),
      t('houseDetail.september'),
      t('houseDetail.october'),
      t('houseDetail.november'),
      t('houseDetail.december'),
    ];
    return monthNames[month];
  };

  if (loading) {
    return (
      <div className="house-detail-container">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>{t('houseDetail.loading')}</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="house-detail-container">
        <div className="error-container glass-panel">
          <p className="error-message">{error}</p>
          {!error.includes('Session') && (
            <button className="glass-button primary" onClick={loadHouseData}>
              {t('houseDetail.retry')}
            </button>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="house-detail-container">
      <div className="house-detail-header">
        <button className="glass-button back-button" onClick={() => navigate('/houses')}>
          ← {t('houseDetail.backToHouses')}
        </button>
      </div>

      {message.text && (
        <div className={`message-banner ${message.type}`}>
          {message.text}
        </div>
      )}

      <div className="house-detail-layout">
        {renderHouseInfo()}

        <div className="calendar-container glass-panel">
          <h3>{t('houseDetail.calendar')}</h3>
          <p className="calendar-subtitle">{t('houseDetail.selectDays')}</p>

          <div className="calendar-header">
            <button
              className="month-nav-button"
              onClick={() => changeMonth(-1)}
              aria-label={t('houseDetail.previousMonth')}
            >
              ←
            </button>
            <h4 className="current-month">
              {getMonthName(currentMonth.getMonth())} {currentMonth.getFullYear()}
            </h4>
            <button
              className="month-nav-button"
              onClick={() => changeMonth(1)}
              aria-label={t('houseDetail.nextMonth')}
            >
              →
            </button>
          </div>

          <div className="calendar-days-header">
            <div className="day-label">{t('houseDetail.sunday')}</div>
            <div className="day-label">{t('houseDetail.monday')}</div>
            <div className="day-label">{t('houseDetail.tuesday')}</div>
            <div className="day-label">{t('houseDetail.wednesday')}</div>
            <div className="day-label">{t('houseDetail.thursday')}</div>
            <div className="day-label">{t('houseDetail.friday')}</div>
            <div className="day-label">{t('houseDetail.saturday')}</div>
          </div>

          <div className="calendar-grid">
            {renderCalendar()}
          </div>

          <div className="calendar-legend">
            <h4>{t('houseDetail.legend')}</h4>
            <div className="legend-items">
              <div className="legend-item">
                <span className="legend-color available"></span>
                <span>{t('houseDetail.available')}</span>
              </div>
              <div className="legend-item">
                <span className="legend-color reserved-by-user"></span>
                <span>{t('houseDetail.yourReservation')}</span>
              </div>
              <div className="legend-item">
                <span className="legend-color reserved-by-other"></span>
                <span>{t('houseDetail.reservedByUser')}</span>
              </div>
              <div className="legend-item">
                <span className="legend-color selected"></span>
                <span>{t('houseDetail.selectedForReservation')}</span>
              </div>
            </div>
          </div>

          {renderReservationControls()}
        </div>
      </div>

      {renderPaymentSummary()}
    </div>
  );
};

export default HouseDetail;
