import { graphql } from 'react-relay';

export const relayQueries = {
  getAllUsersAndTodos: graphql`
    query relayQueriesGetAllUsersAndTodosQuery {
      user {
        user_id
        user_email
        user_profile
        created_at
        todos {
          todo_id
          user_id
          user {
            user_id
          }
          title
          description
          finished_at
          created_at
          updated_at
        }
      }
    }
  `,
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
