const path = require('path');

const clientWebSrc = path.join(__dirname, '../client-web/src');

module.exports = {
  schema: [
    {
      // 'https://hasura.io/learn/graphql': { headers: { Authorization: 'Bearer TODO', }, },
      'http://localhost:61081/v1/graphql': { headers: { Authorization: 'Bearer TODO', }, },
    },
  ],
  documents: [
    `${clientWebSrc}/**/*.tsx`,
    `${clientWebSrc}/**/*.ts`,
  ],
  overwrite: true,
  generates: {
    [`${clientWebSrc}/generated/graphql/index.tsx`]: {
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
    [`${clientWebSrc}/generated/graphql.schema.json`]: {
      plugins: ['introspection'],
    },
  },
};
