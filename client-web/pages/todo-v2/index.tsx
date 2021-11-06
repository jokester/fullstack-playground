import React from 'react';
import { Tabs, TabList, TabPanels, Tab, TabPanel, Heading } from '@chakra-ui/react';
import { UserTodoApp } from '../../src/todo-app-v2/user-todo-app';
import { inServer } from '../../src/config/build-env';

const TodoListPage: React.FC = () => {
  return (
    <div>
      <Heading className="mx-auto">
        Demo:
        <a href="https://github.com/jokester/fullstack-playground" target="_blank" className="mx-2" rel="noreferrer">
          https://github.com/jokester/fullstack-playground
        </a>
      </Heading>
      <hr className="my-4" />
      <Tabs className="mx-auto">
        <TabList>
          <Tab>UserTodoApp (openapi)</Tab>
        </TabList>
        <TabPanels>
          <TabPanel>
            <div className="p-2">{!inServer && <UserTodoApp />}</div>
          </TabPanel>
        </TabPanels>
      </Tabs>
    </div>
  );
};

export default TodoListPage;
