import { fetchQuery, requestSubscription } from 'react-relay';
import { graphUserTodoApi } from '../graphql-relay/graph-user-todo-api';
import {
  graphUserTodoApiListMyTodosQuery$variables,
  graphUserTodoApiListMyTodosQuery,
  graphUserTodoApiListMyTodosQuery$data,
} from '../graphql-relay/generated/graphUserTodoApiListMyTodosQuery.graphql';
import { createDemoRelayEnv, createRelayEnv } from '../graphql-relay/relay-deps';
import { useCallback, useEffect, useRef } from 'react';
import { CredApi } from './use-user-cred';
import { defaultApiEndpoints } from '../config/build-env';
import type RelayModernEnvironment from 'relay-runtime/lib/store/RelayModernEnvironment';

export function useUserTodoGqlApi(credApi: CredApi, gqlEndpoint = defaultApiEndpoints.graphql) {
  const cred = credApi.getCurrent();

  const envRef = useRef<null | RelayModernEnvironment>(null);

  useEffect(() => {
    if (!(gqlEndpoint && cred)) {
      envRef.current = null;
      return;
    }
    const relayEnv = createRelayEnv(defaultApiEndpoints.graphql, cred.accessToken);
    console.debug('relayEnv', (envRef.current = relayEnv));

    return () => {
      // TODO: what
    };
  }, [gqlEndpoint, cred]);

  const listQuery = useCallback(
    (userId?: number) => {
      if (!envRef.current) {
        throw new Error('relay env not available');
      }
      if (!cred) {
        throw new Error(`cred not available`);
      }
      fetchQuery<graphUserTodoApiListMyTodosQuery>(envRef.current, graphUserTodoApi.listMyTodos, {
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
