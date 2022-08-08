import React from 'react';
import Document, { Html, Main, NextScript, Head } from 'next/document';
import { fontawesomeCss } from '@jokester/ts-commonutil/lib/react/component/font-awesome';

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
          {fontawesomeCss}
        </Head>
        <body>
          <Main />
          <NextScript />
        </body>
      </Html>
    );
  }
}
