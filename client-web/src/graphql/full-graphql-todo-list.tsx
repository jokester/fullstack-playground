import React from 'react';
import {
  GetAllTodosV1Query,
  GetAllTodosV1QueryResult,
  Insert_TodosDocument,
  useGetAllTodosV1Query,
  useInsert_TodosMutation,
} from '../generated/graphql';
import { Button, List, ListItem } from '@chakra-ui/react';
import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { callRxApiClient } from '../openapi-rx/use-rx-api';
import { Todo } from '../generated/openapi-rx';
import { useMutation } from '@apollo/react-hooks';

type ElementOf<MaybeArray> = NonNullable<MaybeArray> extends ReadonlyArray<infer Elem> ? Elem : unknown;
type GqlTodo = ElementOf<GetAllTodosV1Query['todos_v1']>;

export const FullGraphqlTodoList: React.FC = () => {
  const { loading, error, data, refetch } = useGetAllTodosV1Query();
  const [lock, lockDepth] = useConcurrencyControl();

  const [addTodo] = useInsert_TodosMutation();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error || !data) {
    return <div>Error...</div>;
  }

  const onReload = () => refetch();

  const onCreate = () =>
    lock(async (mounted) => {
      const created = await addTodo({
        variables: {
          title: `title-${Date.now()}`,
          desc: 'desc',
        },
      });
      created.data?.insert_todos_v1?.returning;

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
        {data.todos_v1.map((item) => (
          <ListItem key={item.todo_id}>
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
