import React, { useState } from 'react';
import { Tabs, TabList, TabPanels, Tab, TabPanel } from '@chakra-ui/react';
import { FetchTodoList } from '../src/openapi-fetch/fetch-todo-list';
import { Heading } from '@chakra-ui/react';
import { RxTodoList } from '../src/openapi-rx/rx-todo-list';
import { FullGraphqlTodoList } from '../src/graphql/full-graphql-todo-list';
import { ApolloClient } from 'apollo-client';
import { HttpLink } from 'apollo-link-http';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { ApolloProvider } from '@apollo/react-hooks';
import useConstant from 'use-constant';

const ApolloContext: React.FC = (props) => {
  const apollo: any = useConstant(
    () =>
      new ApolloClient({
        link: new HttpLink({
          uri: 'http://localhost:61081/v1/graphql',
        }),
        cache: new InMemoryCache(),
      }),
  );
  return <ApolloProvider client={apollo}>{props.children}</ApolloProvider>;
};

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
            <ApolloContext>
              <FullGraphqlTodoList />
            </ApolloContext>
          </TabPanel>
        </TabPanels>
      </Tabs>
    </div>
  );
};

export default TodoListPage;
