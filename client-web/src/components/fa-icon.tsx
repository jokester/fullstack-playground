import React from 'react';

export const FaIcon: React.FC<{ className?: string; icon: string }> = ({ className, icon }) => {
  return <i className={`fa fa-${icon} ${className}`} />;
};
