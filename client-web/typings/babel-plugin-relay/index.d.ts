import type { graphql } from 'react-relay';
declare module 'babel-plugin-relay/macro' {
  const macro: typeof graphql;
  export = macro;
}
