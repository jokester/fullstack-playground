import { graphql } from 'react-relay';

export const exampleQueries = {
  listTodos: graphql`
    query exampleQueriesListTodosQuery {
      todos {
        todo_id
        title
        desc
        finished_at
        created_at
        updated_at
      }
    }
  `,
} as const;
