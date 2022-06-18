import { useState, useEffect } from 'react';

export function useInServer(): boolean {
  const [inServer, setInServer] = useState(true);
  useEffect(() => {
    setInServer(false);
  }, []);

  return inServer;
}
