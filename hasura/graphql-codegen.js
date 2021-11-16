const path = require('path');

const clientWebSrc = path.join(__dirname, '../client-web/src');
const specX = 

module.exports = {
  // see https://www.graphql-code-generator.com/docs/getting-started/codegen-config
  schema: [
    '/Users/wangguan/sidekick/fullstack-playground/api-spec/schema.graphql',
  ],
  documents: [
    `${clientWebSrc}/**/*.tsx`,
    `${clientWebSrc}/**/*.ts`,
    `!${clientWebSrc}/generated/**/*`,
  ],
  overwrite: true,
  generates: {
    [`${clientWebSrc}/generated/graphql-codegen/index.tsx`]: {
      plugins: [
        'typescript',
        'typescript-operations',
        'typescript-react-apollo',
      ],
      config: {
        skipTypename: false,
        withHooks: true,
        withHOC: false,
        withComponent: false,
      },
    },
    // [`${clientWebSrc}/generated/graphql.schema.json`]: { plugins: ['introspection'], },
  },
};
