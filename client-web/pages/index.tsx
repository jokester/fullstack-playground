import React, { useState } from 'react';
import { FetchTodoList } from '../src/openapi-fetch/fetch-todo-list';
import { Heading } from '@chakra-ui/react';
import { RxTodoList } from '../src/openapi-rx/rx-todo-list';

const TodoListPage: React.FC = () => {
  const [rev, setRev] = useState(0);
  return (
    <div>
      <div className="p-2">
        <Heading as="h1">TodoList (openapi + fetch)</Heading>
        <FetchTodoList revision={rev} onMutated={() => setRev(1 + rev)} />
      </div>
      <hr className="my-2" />
      <div className="p-2">
        <Heading as="h1">TodoList (openapi + rxjs)</Heading>
        <RxTodoList revision={rev} onMutated={() => setRev(1 + rev)} />
      </div>
    </div>
  );
};

export default TodoListPage;
