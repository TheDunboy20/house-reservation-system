import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { houseAPI } from '../../services/api';
import './Houses.css';

const AddHouse = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    address: '',
    availableFrom: '',
    availableTo: '',
    price: '',
    image: null,
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        // Convert to base64 and remove the data URL prefix
        const base64String = reader.result.split(',')[1];
        setFormData((prev) => ({
          ...prev,
          image: base64String,
        }));
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Convert price to cents for backend
      const priceInCents = Math.round(parseFloat(formData.price) * 100);

      const houseData = {
        name: formData.name,
        description: formData.description,
        address: formData.address,
        availableFrom: formData.availableFrom,
        availableTo: formData.availableTo,
        priceInCents: priceInCents,
        image: formData.image,
        deleted: false,
      };

      await houseAPI.create(houseData);
      navigate('/houses');
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to create house. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/houses');
  };

  return (
    <div className="add-house-container">
      <div className="container">
        <div className="add-house-box glass fade-in">
          <div className="add-house-header">
            <h1 className="add-house-title">Add New House</h1>
            <button className="glass-button" onClick={handleCancel}>
              Cancel
            </button>
          </div>

          <form onSubmit={handleSubmit} className="add-house-form">
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">House Name *</label>
                <input
                  type="text"
                  name="name"
                  className="glass-input"
                  placeholder="Beautiful Beach House"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Price per Night (USD) *</label>
                <input
                  type="number"
                  name="price"
                  className="glass-input"
                  placeholder="150.00"
                  step="0.01"
                  min="0"
                  value={formData.price}
                  onChange={handleInputChange}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Address *</label>
              <input
                type="text"
                name="address"
                className="glass-input"
                placeholder="123 Ocean Drive, Miami Beach, FL 33139"
                value={formData.address}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Description *</label>
              <textarea
                name="description"
                className="glass-input"
                placeholder="Describe your house..."
                rows="4"
                value={formData.description}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Available From *</label>
                <input
                  type="date"
                  name="availableFrom"
                  className="glass-input"
                  value={formData.availableFrom}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Available To *</label>
                <input
                  type="date"
                  name="availableTo"
                  className="glass-input"
                  value={formData.availableTo}
                  onChange={handleInputChange}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">House Image</label>
              <input
                type="file"
                accept="image/*"
                className="glass-input file-input"
                onChange={handleImageChange}
              />
              <p className="form-hint">Optional: Upload a photo of your house</p>
            </div>

            {error && <div className="error-message">{error}</div>}

            <div className="form-actions">
              <button
                type="button"
                className="glass-button"
                onClick={handleCancel}
              >
                Cancel
              </button>
              <button
                type="submit"
                className="glass-button primary"
                disabled={loading}
              >
                {loading ? 'Creating...' : 'Create House'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddHouse;
