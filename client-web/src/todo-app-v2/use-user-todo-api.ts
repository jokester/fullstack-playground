import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { CredApi } from './use-user-cred';
import { useEffect, useState } from 'react';
import { TodoItem } from './generated';

export function useUserTodoApi(credApi: CredApi) {
  const [withLock, lockDepth] = useConcurrencyControl(1);

  const [todos, setTodos] = useState<null | TodoItem[]>(null);

  const reloadTodo = async () => {
    const v = await credApi.callApiWithCred((api, user) => api.getUserTodoApiUsersUseridTodos({ userId: user.userId }));
    console.debug('reloadTodo', v);
    setTodos(v.todos || []);
  };

  useEffect(() => {
    setTimeout(reloadTodo);
  }, []);

  const createTodo = (title: string, desc: string) =>
    withLock(async () => {
      const v = await credApi.callApiWithCred((api, user) =>
        api.postUserTodoApiUsersUseridTodos({
          userId: user.userId,
          createTodoRequest: { title, description: desc },
        }),
      );
      await reloadTodo();
    });

  const updateTodo = (todoItem: TodoItem) =>
    withLock(async () => {
      const v = await credApi.callApiWithCred((api, user) =>
        api.patchUserTodoApiUsersUseridTodosTodoid({ userId: user.userId, todoId: todoItem.todoId, todoItem }),
      );
      await reloadTodo();
    });

  const deleteTodo = (todo: TodoItem) =>
    withLock(async () => {
      const v = await credApi.callApiWithCred((api, user) =>
        api.deleteUserTodoApiUsersUseridTodosTodoid({ userId: user.userId, todoId: todo.todoId }),
      );
      await reloadTodo();
    });

  return {
    todos,
    createTodo,
    updateTodo,
    deleteTodo,
    reloadTodo,
    lockDepth,
  } as const;
}
