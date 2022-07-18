import Head from 'next/head';

import React from 'react';

export const DefaultMeta: React.FC<{ title?: string }> = (props) => {
  return (
    <Head>
      <title key="head-title">{props.title ?? 'untitled'}</title>
    </Head>
  );
};
