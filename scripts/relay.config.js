const path = require('path')

const clientWebSrc = path.join(__dirname, '../client-web/src');

module.exports = {
  // Configuration options accepted by the `relay-compiler` command-line tool and `babel-plugin-relay`.
  src: clientWebSrc,
  schema: `${clientWebSrc}/generated/schema.graphql`,
  exclude: ['**/node_modules/**', '**/__mocks__/**', '**/generated/**', '**/.next/**', '**/test/**'],
  extensions: ['ts', 'tsx'],
  artifactDirectory: `${clientWebSrc}/generated/graphql-relay`,
  language: 'typescript',
};
