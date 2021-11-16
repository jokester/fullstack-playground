import { graphql } from 'react-relay';

export const graphTodoApi = {
  listTodo: graphql`
    query graphTodoApiListTodoQuery {
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
  subscribeTodo: graphql`
    subscription graphTodoApiListTodoSubscription {
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
