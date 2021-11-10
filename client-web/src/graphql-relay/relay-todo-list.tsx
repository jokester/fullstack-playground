import { FC } from 'react';
import { QueryRenderer } from 'react-relay';
import { relayEnv } from './relay-deps';
import { Button } from '@chakra-ui/react';
import { userTodoGraphListOwnTodosQuery } from '../generated/graphql-relay/userTodoGraphListOwnTodosQuery.graphql';
import { userTodoGraph } from './user-todo-graph';

export const RelayTodoList: FC<{ userId: number }> = (props) => {
  return (
    <QueryRenderer<userTodoGraphListOwnTodosQuery>
      environment={relayEnv}
      variables={{ userId: props.userId }}
      query={userTodoGraph.listOwnTodos}
      render={(result) => {
        const inner = result.error ? (
          <p>ERROR: {result.error.message}</p>
        ) : result.props ? (
          <ul>
            {result.props.todo.map((todo) => (
              <li key={todo.todo_id}>
                title: {todo.title}
                <br />
                desc: {todo.description}
                <br />
                finished_at: {todo.finished_at}
              </li>
            ))}
          </ul>
        ) : (
          'loading'
        );

        return (
          <div>
            {inner}

            <hr />
            <Button size="sm" onClick={result.retry || undefined}>
              reload
            </Button>
          </div>
        );
      }}
    />
  );
};
