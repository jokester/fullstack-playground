import { graphql } from 'react-relay';

export const graphUserTodoApi = {
  listMyTodos: graphql`
    query graphUserTodoApiListMyTodosQuery($userId: Int!) {
      user: user_todo_users(where: { user_id: { _eq: $userId } }) {
        updated_at
        user_id
      }
      todos: user_todo_user_todos {
        user_id
        todo_id
        title
        finished_at
        description
        created_at
        updated_at
      }
    }
  `,
} as const;
