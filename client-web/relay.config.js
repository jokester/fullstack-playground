/* eslint-disable @typescript-eslint/no-var-requires */
const path = require('node:path');
const fs = require('node:fs')

const clientWebRoot = __dirname;
const clientWebSrc = path.join(__dirname, './src');
const schema = path.join(clientWebRoot, '../api-spec/schema.graphql');

const extra = process.env.RELAY_EXPORT_QUERIES && {
  persistConfig: {
    file: path.join(clientWebRoot, '../api-spec/relay-queries.json'),
  },
  artifactDirectory: fs.mkdtempSync('relay-discarded-artifacts'),
}

module.exports = {
  // Configuration options accepted by the `relay-compiler` command-line tool and `babel-plugin-relay`.
  src: clientWebSrc,
  schema,
  exclude: ['**/node_modules/**', '**/__mocks__/**', '**/generated/**', '**/.next/**', '**/test/**'],
  artifactDirectory: `${clientWebSrc}/graphql-relay/generated`,
  language: 'typescript',
  // NOT using persistOutput:
  // we don't have a queryId-aware server, and only use persistOutput to save-as query texts
  // persistOutput,
  customScalars: {
    timestamptz: 'Date',
  },
  ...extra
};
