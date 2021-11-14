import { fetchQuery } from 'react-relay';
import { graphTodoApi } from '../graphql-relay/graph-todo-api';
import { createRelayEnv } from '../graphql-relay/relay-deps';
import { graphTodoApiListTodoQuery } from '../graphql-relay/generated/graphTodoApiListTodoQuery.graphql';
import { Todo } from './generated';
import { useMemo } from 'react';

function convertGraphqlRes(grahqlRes: graphTodoApiListTodoQuery['response']['todos'][number]): Todo {
  return {
    title: grahqlRes.title,
    id: grahqlRes.todo_id,
    desc: grahqlRes.desc,
    finished: !!grahqlRes.finished_at,
    updatedAt: grahqlRes.updated_at,
  };
}

export function useTodoGqlApi(graphqlEndPoint: string) {
  const queryDeps = useMemo(() => {
    const relayEnv = createRelayEnv(graphqlEndPoint);

    const listQuery = fetchQuery<graphTodoApiListTodoQuery>(relayEnv, graphTodoApi.listTodo, {});
    return {
      relayEnv,
      listQuery,
    };
  }, [graphqlEndPoint]);

  const onFetchGql = () =>
    queryDeps.listQuery.toPromise().then((fetched) => (fetched?.todos ?? []).map(convertGraphqlRes));

  const onSubscribeGql = (onValue: (value: Todo[]) => void) =>
    queryDeps.listQuery.subscribe({
      next(graphqlValue) {
        onValue(graphqlValue.todos.map(convertGraphqlRes));
      },
    });

  return {
    onFetchGql,
    onSubscribeGql,
  };
}
