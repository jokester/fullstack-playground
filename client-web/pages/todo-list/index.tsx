import React from 'react';
import { TodoAppV1 } from '../../src/todo-app-v1/todo-app-v1';
import { DefaultMeta } from '../../src/components/meta/default-meta';
import { useRouter } from 'next/router';

const TodoListAppPage: React.FC = () => {
  const { api: _api } = useRouter().query as { api: string };
  const apiOrigin = _api || 'http://127.0.0.1:8080/stated-openapi/todos';
  return (
    <div>
      <DefaultMeta title="todo list" />
      <div className="max-w-screen-sm mx-auto my-4 px-2 md:px-0">
        <h1 className="text-center">Todo List</h1>
        <TodoAppV1 apiOrigin={apiOrigin} />
      </div>
    </div>
  );
};
export default TodoListAppPage;
