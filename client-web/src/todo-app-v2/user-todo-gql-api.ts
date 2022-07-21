import { fetchQuery, requestSubscription } from 'react-relay';
import { graphUserTodoApi } from '../graphql-relay/graph-user-todo-api';
import {
  graphUserTodoApiListMyTodosQuery$variables,
  graphUserTodoApiListMyTodosQuery,
  graphUserTodoApiListMyTodosQuery$data,
} from '../graphql-relay/generated/graphUserTodoApiListMyTodosQuery.graphql';
import { createDemoRelayEnv, createRelayEnv } from '../graphql-relay/relay-deps';
import { useCallback } from 'react';
import { CredApi } from './use-user-cred';
import { defaultApiEndpoints } from '../config/build-env';

export function useUserTodoGqlApi(credApi: CredApi, gqlEndpoint = defaultApiEndpoints.graphql) {
  const cred = credApi.getCurrent();

  const listQuery = useCallback(
    (userId?: number) => {
      if (!cred) {
        throw new Error(`cred not available`);
      }
      const relayEnv = createRelayEnv(defaultApiEndpoints.graphql, cred.accessToken);
      console.debug('relayEnv', relayEnv);
      fetchQuery<graphUserTodoApiListMyTodosQuery>(relayEnv, graphUserTodoApi.listMyTodos, {
        userId: userId ?? cred.userId,
      })
        .toPromise()
        .then(
          (res) => {
            console.debug('got res', res);
          },
          (err) => {
            console.debug('got err', err);
          },
        );
    },
    [gqlEndpoint, cred],
  );

  return {
    listQuery,
  };
}
