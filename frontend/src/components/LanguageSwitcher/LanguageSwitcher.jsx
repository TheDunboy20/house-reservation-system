import React from 'react';
import { useLanguage } from '../../context/LanguageContext';
import './LanguageSwitcher.css';

const LanguageSwitcher = () => {
  const { language, switchLanguage } = useLanguage();

  return (
    <div className="language-switcher">
      <button
        className={`lang-button ${language === 'en' ? 'active' : ''}`}
        onClick={() => switchLanguage('en')}
      >
        EN
      </button>
      <button
        className={`lang-button ${language === 'sk' ? 'active' : ''}`}
        onClick={() => switchLanguage('sk')}
      >
        SK
      </button>
    </div>
  );
};

export default LanguageSwitcher;
