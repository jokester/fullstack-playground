import { RelayTodoList } from '../src/graphql-relay/relay-todo-list';
import { RelayAdminUi } from '../src/graphql-relay/relay-admin-ui';
import { RelayEnvironmentProvider } from 'relay-hooks';
import React from 'react';
import { relayEnv } from '../src/graphql-relay/relay-deps';

const RelayDemoPage = () => (
  <RelayEnvironmentProvider environment={relayEnv}>
    <div>
      <RelayTodoList />
      <hr />
      <RelayAdminUi />
    </div>
  </RelayEnvironmentProvider>
);

export default RelayDemoPage;
