module.exports = {
  // Configuration options accepted by the `relay-compiler` command-line tool and `babel-plugin-relay`.
  src: './src',
  schema: './../apispec/schema.graphql',
  exclude: ['**/node_modules/**', '**/__mocks__/**', '**/generated/**', '**/.next/**', '**/test/**'],
  extensions: ['ts', 'tsx', 'js', 'jsx'],
  artifactDirectory: './src/generated/graphql-relay',
  language: 'typescript',
};
