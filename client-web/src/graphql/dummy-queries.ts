import gql from 'graphql-tag';

/**
 * dummy queries for graphql-codegen
 */
if (1) {
  throw new Error('do not import this file');
}

namespace todosApiV1 {
  const getAllTodosV1 = gql`
    query getAllTodosV1 {
      todo_v1 {
        todo_id
        title
        desc
        finished_at
        created_at
        updated_at
      }
    }
  `;

  const createTodoV1 = gql`
    mutation insert_todos($title: String!, $desc: String!) {
      insert_todo_v1(objects: { title: $title, desc: $desc }) {
        affected_rows
        returning {
          todo_id
          title
          desc
          finished_at
          created_at
          updated_at
        }
      }
    }
  `;
}
