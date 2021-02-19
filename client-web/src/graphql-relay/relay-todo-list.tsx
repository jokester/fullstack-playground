import { FC } from 'react';
import { QueryRenderer } from 'react-relay';
import { relayEnv } from './relay-deps';
import { relayQueries } from './relay-queries';
import { PreJson } from '../dummy/pre-json';
import { relayQueriesGetAllTodosQuery } from '../generated/graphql-relay/relayQueriesGetAllTodosQuery.graphql';
import { relayQueriesFindTodoByUserQuery } from '../generated/graphql-relay/relayQueriesFindTodoByUserQuery.graphql';

export const RelayTodoList: FC = () => {
  return (
    <QueryRenderer<relayQueriesGetAllTodosQuery>
      environment={relayEnv}
      variables={{}}
      query={relayQueries.getAllTodos}
      render={({ error, props }) => {
        if (error) {
          return <p>{error.message}</p>;
        } else if (props) {
          return <PreJson value={props.todo} />;
        }
        return <p>Loading your Relay Modern data...</p>;
      }}
    />
  );
};

export const RelayTodoList2: FC = () => {
  return (
    <QueryRenderer<relayQueriesFindTodoByUserQuery>
      environment={relayEnv}
      query={relayQueries.findTodoByUser}
      variables={{ userId: 1 }}
      render={(result) => {
        if (result.error) {
          return <p>{result.error.message}</p>;
        }
        if (result.props) {
          return <PreJson value={result.props} />;
        }
        return null;
      }}
    />
  );
};
