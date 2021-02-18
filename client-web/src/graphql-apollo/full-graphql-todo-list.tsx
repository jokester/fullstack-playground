import React from 'react';
import { useGetAllTodosQuery, useGetAllUsersQuery } from '../generated/graphql-codegen';
import { Button, List, ListItem } from '@chakra-ui/react';
import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { callRxApiClient } from '../openapi-rx/use-rx-api';
import { Todo } from '../generated/openapi-rx';

type ElementOf<MaybeArray> = NonNullable<MaybeArray> extends ReadonlyArray<infer Elem> ? Elem : unknown;

export const FullGraphqlTodoList: React.FC = () => {
  const todos = useGetAllTodosQuery();
  const users = useGetAllUsersQuery();
  const [lock, lockDepth] = useConcurrencyControl();

  if (todos.loading || users.loading) {
    return <div>Loading...</div>;
  }

  if (todos.error || !users.data) {
    return <div>Error...</div>;
  }

  const onReload = () => {
    todos.refetch();
    users.refetch();
  };

  const onCreate = () =>
    lock(async (mounted) => {
      const created = await callRxApiClient((api) =>
        api.postTodos({
          todoCreateRequest: {
            title: `title-${Date.now()}`,
            desc: 'desc',
          },
        }),
      );
      onReload();
    });

  const onDelete = (todoId: number) =>
    lock(async () => {
      const x = await callRxApiClient((api) => api.deleteTodosP1({ p1: todoId }));
      onReload();
    });

  const onUpdate = (todoId: number, modified: Todo) =>
    lock(async () => {
      const x = await callRxApiClient((api) => api.patchTodosTodoP1({ p1: todoId, todo: modified }));
      onReload();
    });

  return (
    <div>
      <div>
        <Button onClick={onCreate} isLoading={lockDepth > 0}>
          create
        </Button>
      </div>
      <List styleType="disc " padding={8}>
        {todos.data?.todo.map((item) => (
          <ListItem key={item.title}>
            <p>
              id={item.todo_id} / title={item.title} / {item.finished_at ? 'finished' : 'todo'}
            </p>
            <br />
            <Button onClick={() => onDelete(item.todo_id)} isLoading={lockDepth > 0}>
              Delete
            </Button>
            <Button
              onClick={() =>
                onUpdate(item.todo_id, {
                  ...item,
                  id: item.todo_id,
                  finished: !item.finished_at,
                  desc: item.description,
                })
              }
              isLoading={lockDepth > 0}
            >
              {item.finished_at ? 'change to todo' : 'change to finish'}
            </Button>
          </ListItem>
        ))}
      </List>
    </div>
  );
};
