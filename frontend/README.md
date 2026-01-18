# House Reservation System - Frontend

A modern React application with glassmorphism UI design for managing house reservations.

## Features

- User authentication (Register/Login)
- View all available houses
- Add new houses
- Modern glassmorphism (liquid glass) design
- Responsive layout
- Session-based authentication

## Tech Stack

- **React 18** - UI library
- **Vite** - Build tool and dev server
- **React Router** - Client-side routing
- **Axios** - HTTP client
- **CSS3** - Glassmorphism styling

## Getting Started

### Prerequisites

- Node.js 18+ installed
- Backend server running on http://localhost:8080

### Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm run dev
```

The application will be available at http://localhost:3000

### Build for Production

```bash
npm run build
```

The production-ready files will be in the `dist` folder.

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── Auth/
│   │   │   ├── Login.jsx
│   │   │   ├── Register.jsx
│   │   │   └── Auth.css
│   │   ├── Houses/
│   │   │   ├── HouseList.jsx
│   │   │   ├── HouseCard.jsx
│   │   │   ├── AddHouse.jsx
│   │   │   └── Houses.css
│   │   └── ProtectedRoute.jsx
│   ├── context/
│   │   └── AuthContext.jsx
│   ├── services/
│   │   └── api.js
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── index.html
├── vite.config.js
└── package.json
```

## Features Overview

### Authentication
- Users can register with a username and password
- Login with session-based authentication
- Protected routes that require authentication
- Automatic logout on session expiration

### House Management
- View all available houses in a beautiful grid layout
- Each house card displays:
  - House image (if available)
  - Name and address
  - Description
  - Price per night
  - Availability dates
- Add new houses with:
  - Name, description, and address
  - Price per night
  - Availability date range
  - Optional house image upload

### Design
- Modern glassmorphism (liquid glass) effect
- Animated gradient background
- Smooth transitions and hover effects
- Fully responsive design
- Beautiful loading states

## API Integration

The frontend connects to the Spring Boot backend via proxy configuration in `vite.config.js`:

- `/auth/*` - Authentication endpoints
- `/houses/*` - House management endpoints
- `/reservations/*` - Reservation endpoints

All requests include credentials for session-based authentication.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

This project is part of the House Reservation System.
