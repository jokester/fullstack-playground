import React from 'react';
import Document, { Html, Main, NextScript, Head } from 'next/document';

export default class CustomDocument extends Document {
  render(): React.ReactElement {
    return (
      <Html>
        <Head>
          <link
            rel="stylesheet"
            key="css-tailwindcss"
            href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.2/dist/tailwind.min.css"
            integrity="sha256-KwBcfPYYUP4pXG0aiIA8nTSuAqRzRWdtoHQktxvMVf4="
            crossOrigin="anonymous"
          />

          <link
            key="css-fontawesome4"
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css"
            integrity="sha256-eZrrJcwDc/3uDhsdt61sL2oOBY362qM3lon1gyExkL0="
            crossOrigin="anonymous"
          />
          <meta
            key="meta-viewport"
            name="viewport"
            content="width=device-width, initial-scale=1,maximum-scale=1.5,minimum-scale=1"
          />
        </Head>
        <body>
          <Main />
          <NextScript />
        </body>
      </Html>
    );
  }
}
