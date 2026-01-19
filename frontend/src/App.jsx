import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { LanguageProvider } from './context/LanguageContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import HouseList from './components/Houses/HouseList';
import AddHouse from './components/Houses/AddHouse';
import HouseDetail from './components/Houses/HouseDetail';

function App() {
  return (
    <Router>
      <LanguageProvider>
        <AuthProvider>
          <Routes>
            {/* Public routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Protected routes */}
            <Route
              path="/houses"
              element={
                <ProtectedRoute>
                  <HouseList />
                </ProtectedRoute>
              }
            />
            <Route
              path="/houses/new"
              element={
                <ProtectedRoute>
                  <AddHouse />
                </ProtectedRoute>
              }
            />
            <Route
              path="/houses/:id"
              element={
                <ProtectedRoute>
                  <HouseDetail />
                </ProtectedRoute>
              }
            />

            {/* Default redirect */}
            <Route path="/" element={<Navigate to="/houses" replace />} />
            <Route path="*" element={<Navigate to="/houses" replace />} />
          </Routes>
        </AuthProvider>
      </LanguageProvider>
    </Router>
  );
}

export default App;
