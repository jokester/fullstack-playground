import { graphql } from 'react-relay';

export const userTodoGraph = {
  listOwnTodos: graphql`
    query userTodoGraphListOwnTodosQuery($userId: Int!) {
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
