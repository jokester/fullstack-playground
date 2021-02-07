import React from 'react';
import { callRxApiClient as callFetchApiClient, useApiResult as useFetchApiResult } from './use-rx-api';
import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { Todo } from '../generated/openapi-fetch';
import { Button, List, ListItem } from '@chakra-ui/react';

export const RxTodoList: React.FC<{ revision?: number; onMutated?(): void }> = (props) => {
  const [todoList, reloadList] = useFetchApiResult(
    (api) =>
      api
        .getTodos()
        .toPromise()
        .then((todos) => todos.sort((a, b) => b.id - a.id)),
    [props.revision],
  );

  const [lock, lockDepth] = useConcurrencyControl();

  const onCreate = () =>
    lock(async (mounted) => {
      const created = await callFetchApiClient((api) =>
        api.postTodos({
          todoCreateRequest: {
            title: `title-${Date.now()}`,
            desc: 'desc',
          },
        }),
      );

      props.onMutated?.();
      await reloadList();
    });

  const onDelete = (item: Todo) =>
    lock(async () => {
      const x = await callFetchApiClient((api) => api.deleteTodosP1({ p1: item.id }));
      props.onMutated?.();
      reloadList();
    });

  const onUpdate = (modified: Todo) =>
    lock(async () => {
      const x = await callFetchApiClient((api) => api.patchTodosTodoP1({ p1: modified.id, todo: modified }));
      props.onMutated?.();
      reloadList();
    });

  return (
    <div>
      <div>
        <Button onClick={onCreate} isLoading={lockDepth > 0}>
          create
        </Button>
      </div>
      <List styleType="disc " padding={8}>
        {todoList.fulfilled &&
          todoList.value.map((item) => (
            <ListItem key={item.id}>
              <p>
                id={item.id} / title={item.title} / {item.finished ? 'finished' : 'todo'}
              </p>
              <br />
              <Button onClick={() => onDelete(item)} isLoading={lockDepth > 0}>
                Delete
              </Button>
              <Button onClick={() => onUpdate({ ...item, finished: !item.finished })} isLoading={lockDepth > 0}>
                {item.finished ? 'change to todo' : 'change to finish'}
              </Button>
            </ListItem>
          ))}
      </List>
    </div>
  );
};
