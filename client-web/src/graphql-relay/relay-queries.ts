import { QueryRenderer, graphql } from 'react-relay';

if (1) {
  throw new Error('do not import');
}

export const relayQueries = {
  getAllTodos: graphql`
    query relayQueriesGetAllTodosQuery {
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

  findTodoByUser: graphql`
    query relayQueriesFindTodoByUserQuery($userId: Int!) {
      todo(where: { user_id: { _eq: $userId } }) {
        todo_id
        title
        description
        created_at
        updated_at
        finished_at
      }
    }
  `,
} as const;
