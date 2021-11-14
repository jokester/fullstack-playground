import React from 'react';
import { TodoAppV1 } from '../../src/todo-app-v1/todo-app-v1';
import { DefaultMeta } from '../../src/components/meta/default-meta';

const TodoListAppPage: React.FC = () => {
  return (
    <div>
      <DefaultMeta title="todo list" />
      <div className="w-screen-md mx-auto my-4">
        <h1 className="text-center">Todo List</h1>
        <TodoAppV1 apiOrigin="http://127.0.0.1:8080/stateless-openapi/todos" />
      </div>
    </div>
  );
};
export default TodoListAppPage;
