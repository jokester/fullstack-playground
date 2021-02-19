import React, { useState } from 'react';
import { Tabs, TabList, TabPanels, Tab, TabPanel } from '@chakra-ui/react';
import { FetchTodoList } from '../src/openapi-fetch/fetch-todo-list';
import { Heading } from '@chakra-ui/react';
import { RxTodoList } from '../src/openapi-rx/rx-todo-list';

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
          <Tab>OpenAPI (fetch)</Tab>
          <Tab>OpenAPI (RxJS)</Tab>
          <Tab>GraphQL (Apollo)</Tab>
          <Tab>GraphQL (Relay)</Tab>
        </TabList>
        <TabPanels>
          <TabPanel>
            <div className="p-2">
              <FetchTodoList revision={rev} onMutated={onReload} />
            </div>
          </TabPanel>
          <TabPanel>
            <div className="p-2">
              <RxTodoList revision={rev} onMutated={onReload} />
            </div>
          </TabPanel>
          <TabPanel>
          </TabPanel>
        </TabPanels>
      </Tabs>
    </div>
  );
};

export default TodoListPage;
