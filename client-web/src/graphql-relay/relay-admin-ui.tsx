import { useQuery } from 'relay-hooks';
import { relayQueriesGetAllUsersAndTodosQuery } from '../generated/graphql-relay/relayQueriesGetAllUsersAndTodosQuery.graphql';
import { relayQueries } from './relay-queries';
import { PreJson } from '../dummy/pre-json';
import { useInterval } from 'react-use';

export const RelayAdminUi = () => {
  const x = useQuery<relayQueriesGetAllUsersAndTodosQuery>(relayQueries.getAllUsersAndTodos, undefined, {
    fetchPolicy: 'network-only',
  });

  return <PreJson value={x} />;
};
