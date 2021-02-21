import React, { useState } from 'react';
import { Tabs, TabList, TabPanels, Tab, TabPanel } from '@chakra-ui/react';
import { RxUserTodoList } from '../src/openapi-rx/rx-user-todo-list';
import { Heading } from '@chakra-ui/react';
import { FetchUserTodoList } from '../src/openapi-fetch/rx-user-todo-list';

const TodoListPage: React.FC = () => {
  const [rev, setRev] = useState(0);
  const onReload = () => setRev((_) => ++_);
  return (
    <div>
      <Heading className="mx-auto">
        Demo:
        <a href="https://github.com/jokester/fullstack-playground" target="_blank" className="mx-2">
          https://github.com/jokester/fullstack-playground
        </a>
      </Heading>
      <hr className="my-4" />
      <Tabs className="mx-auto">
        <TabList>
          <Tab>OpenAPI (Fetch)</Tab>
          <Tab>GraphQL (Apollo)</Tab>
          <Tab>GraphQL (Relay)</Tab>
        </TabList>
        <TabPanels>
          <TabPanel>
            <div className="p-2">
              <FetchUserTodoList />
            </div>
          </TabPanel>
          <TabPanel></TabPanel>
        </TabPanels>
      </Tabs>
    </div>
  );
};

export default TodoListPage;
