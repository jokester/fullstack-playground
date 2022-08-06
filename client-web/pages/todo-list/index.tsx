import React from 'react';
import { TodoAppV1 } from '../../src/todo-app-v1/todo-app-v1';
import { DefaultMeta } from '../../src/components/meta/default-meta';
import { useRouter } from 'next/router';
import { defaultApiEndpoints } from '../../src/config/build-env';

function wtf() {
  const e = new Error();
  const v = e.cause;
}

const TodoListAppPage: React.FC = () => {
  const params = useRouter().query as { openapi?: string; gql?: string };
  const apiOrigin = (typeof params?.openapi === 'string' && params.openapi) || defaultApiEndpoints.statedOpenAPI;
  const gqlOrigin = (typeof params?.gql === 'string' && params.gql) || defaultApiEndpoints.graphql;
  return (
    <div>
      <DefaultMeta title="Todo List" />
      <div className="max-w-screen-sm mx-auto my-4 px-2 md:px-0">
        <h1 className="text-center">Todo List</h1>
        <div>
          <p className="font-sm">
            <label className="font-xs">OpenAPI endpoint</label>
            {apiOrigin}
          </p>
          <p className="font-sm">
            <label className="font-xs">GraphQL endpoint</label>
            {gqlOrigin}
          </p>
        </div>
        <TodoAppV1 apiOrigin={apiOrigin} graphqlOrigin={gqlOrigin} />
      </div>
    </div>
  );
};
export default TodoListAppPage;
