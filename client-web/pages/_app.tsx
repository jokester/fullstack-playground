import React from 'react';
import App from 'next/app';
import '../src/app.scss';
import { ChakraProvider } from '@chakra-ui/react';

export default class extends App {
  static getInitialProps = App.getInitialProps;

  render() {
    const { Component } = this.props;

    const { pathname, asPath, query } = this.props.router;
    const pageProps: Record<string, unknown> = {
      ...this.props.pageProps,
      route: { pathname, asPath, query },
    };

    return (
      <ChakraProvider>
        <React.StrictMode>
          <Component {...pageProps} />
        </React.StrictMode>
      </ChakraProvider>
    );
  }
}
