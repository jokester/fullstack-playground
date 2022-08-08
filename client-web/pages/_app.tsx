import React from 'react';
import App, { AppProps } from 'next/app';
import '../src/app.scss';
import { ChakraProvider } from '@chakra-ui/react';
import { DefaultMeta } from '../src/components/meta/default-meta';
import Head from 'next/head';

const CustomApp: React.FC<AppProps> & Partial<Pick<typeof App, 'getInitialProps'>> = (props) => {
  const { Component, pageProps } = props;
  return (
    <>
      <Head>
        <meta
          key="meta-viewport"
          name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1.5,minimum-scale=1"
        />
      </Head>
      <DefaultMeta />
      <ChakraProvider>
        <Component {...pageProps} />
      </ChakraProvider>
    </>
  );
};

// CustomApp.getInitialProps = App.getInitialProps;

export default CustomApp;
