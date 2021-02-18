import gql from 'graphql-tag';

/**
 * dummy queries for graphql-apollo-codegen
 */
if (1) {
  throw new Error('do not import this file');
}

namespace todosApiV1 {
  const getAllUsers = gql`
    query getAllUsers {
      user {
        user_id
        user_email
        user_profile
        created_at
        updated_at
      }
    }
  `;

  const getAllTodos = gql`
    query getAllTodos {
      todo {
        user_id
        title
        description
        finished_at
        created_at
        updated_at
      }
    }
  `;
}
