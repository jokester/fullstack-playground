import { fetchQuery, requestSubscription } from 'react-relay';
import { graphTodoApi } from '../graphql-relay/graph-todo-api';
import { createRelayEnv } from '../graphql-relay/relay-deps';
import { graphTodoApiListTodoQuery } from '../graphql-relay/generated/graphTodoApiListTodoQuery.graphql';
import type { Todo } from './generated';
import { useMemo } from 'react';
import { graphTodoApiListTodoSubscription } from '../graphql-relay/generated/graphTodoApiListTodoSubscription.graphql';

function convertGraphqlRes(grahqlRes: graphTodoApiListTodoQuery['response']['todos'][number]): Todo {
  return {
    title: grahqlRes.title,
    id: grahqlRes.todo_id,
    desc: grahqlRes.desc,
    finished: !!grahqlRes.finished_at,
    updatedAt: grahqlRes.updated_at,
  };
}

export function useRelayEnv(graphqlEndPoint?: string, accessToken?: string) {
  return useMemo(() => {
    if (graphqlEndPoint) {
      const relayEnv = createRelayEnv(graphqlEndPoint, accessToken);
      return {
        relayEnv,
      };
    }
    return null;
  }, [graphqlEndPoint, accessToken]);
}

export function useTodoGqlApi(graphqlEndPoint?: string) {
  const queryDeps = useRelayEnv(graphqlEndPoint);

  return useMemo(() => {
    const listQuery =
      queryDeps?.relayEnv && fetchQuery<graphTodoApiListTodoQuery>(queryDeps.relayEnv, graphTodoApi.listTodo, {});

    const onFetchGql = () => listQuery?.toPromise().then((fetched) => (fetched?.todos ?? []).map(convertGraphqlRes));

    const onSubscribeGql = (onValue: (value: Todo[]) => void) =>
      queryDeps?.relayEnv &&
      requestSubscription<graphTodoApiListTodoSubscription>(queryDeps.relayEnv, {
        subscription: graphTodoApi.subscribeTodo,
        variables: {},
        onNext: (graphqlValue) => {
          onValue((graphqlValue?.todos || []).map(convertGraphqlRes));
        },
      });

    return {
      onFetchGql,
      onSubscribeGql,
    };
  }, [queryDeps]);
}
