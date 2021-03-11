import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { Todo } from '../generated/openapi-rx';
import { CredApi } from './use-user-cred';
import { useEffect, useState } from 'react';

export function useUserTodoApi(cred: CredApi) {
  const [withLock, lockDepth] = useConcurrencyControl(1);

  const [todos, setTodos] = useState<null | Todo[]>(null);

  const reloadTodo = async () => {
    const v = await cred.callApiWithCred((api, user) => api.getUserTodoApiUsersUseridTodos({ userId: user!.userId }));
    setTodos(v.todos || []);
  };

  useEffect(() => {
    setTimeout(reloadTodo);
  }, []);

  const createTodo = (title: string, desc: string) =>
    withLock(async () => {
      const v = await cred.callApiWithCred((api, user) =>
        api.postUserTodoApiUsersUseridTodos({ userId: user!.userId, createTodoRequest: { title, description: desc } }),
      );
      await reloadTodo();
    });

  const updateTodo = (todo: Todo) =>
    withLock(async () => {
      const v = await cred.callApiWithCred((api, user) =>
        api.patchUserTodoApiUsersUseridTodosTodoid({ userId: user!.userId, todoId: todo.todoId, todo }),
      );
      await reloadTodo();
    });

  const deleteTodo = (todo: Todo) =>
    withLock(async () => {
      const v = await cred.callApiWithCred((api, user) =>
        api.deleteUserTodoApiUsersUseridTodosTodoid({ userId: user!.userId, todoId: todo.todoId }),
      );
      await reloadTodo();
    });

  return {
    todos,
    createTodo,
    updateTodo,
    deleteTodo,
    lockDepth,
  };
}
