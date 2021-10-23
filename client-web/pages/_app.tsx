import React from 'react';
import App, { AppProps } from 'next/app';
import '../src/app.scss';
import { ChakraProvider } from '@chakra-ui/react';
import { DefaultMeta } from '../src/components/meta/default-meta';

const CustomApp: React.FC<AppProps> & Partial<Pick<typeof App, 'getInitialProps'>> = (props) => {
  const { Component, pageProps } = props;
  return (
    <React.StrictMode>
      <ChakraProvider>
        <DefaultMeta />
        <Component {...pageProps} />
      </ChakraProvider>
    </React.StrictMode>
  );
};

// CustomApp.getInitialProps = App.getInitialProps;

export default CustomApp;
