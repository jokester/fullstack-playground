import { FC } from 'react';
import { QueryRenderer } from 'react-relay';
import { relayEnv } from './relay-deps';
import { relayQueries } from './relay-queries';
import { PreJson } from '../dummy/pre-json';
import { relayQueriesgetAllTodosQuery } from '../generated/graphql-relay/relayQueriesgetAllTodosQuery.graphql';

export const RelayTodoList: FC = () => {
  return (
    <QueryRenderer<relayQueriesgetAllTodosQuery>
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
