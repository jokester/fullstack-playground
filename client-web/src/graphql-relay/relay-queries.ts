import { QueryRenderer, graphql } from 'react-relay';

export const relayQueries = {
  getAllTodos: graphql`
    query relayQueriesgetAllTodosQuery {
      todo {
        todo_id
        user_id
        title
        description
        finished_at
        created_at
        updated_at
      }
    }
  `,
} as const;
