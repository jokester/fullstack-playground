import type { DefaultApi } from './generated';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { CreateTODORequest, DeleteTODORequest, Todo, UpdateTODORequest } from './generated';
import { date } from 'fp-ts';

async function createTodoApiClient(apiOrigin: string): Promise<DefaultApi> {
  const { DefaultApi, Configuration } = await import('./generated');
  return new DefaultApi(new Configuration({ basePath: apiOrigin }));
}

async function callTodoApi<T>(apiOrigin: string, task: (client: DefaultApi) => Promise<T>) {
  return createTodoApiClient(apiOrigin).then(task);
}

export function useHttpTodoApi(apiOrigin: string, autoReload: boolean) {
  const [state, setState] = useState<null | { mode: 'list'; items: Todo[] }>(null);

  const reloadTodos = useCallback(async () => {
    const items = await callTodoApi(apiOrigin, (api) => api.listTODO()).then((_) => _.todos);
    setState({ mode: 'list', items: items ?? [] });
  }, [apiOrigin]);

  const onCreate = async (req: CreateTODORequest) => {
    const created = await callTodoApi(apiOrigin, (api) => api.createTODO(req));
    // setState((_state) => (_state?.mode === 'list' ? { mode: 'list', items: [..._state.items, created] } : null));
    autoReload && (await reloadTodos());
  };

  const onUpdate = async (req: UpdateTODORequest) => {
    const updated = await callTodoApi(apiOrigin, (api) => api.updateTODO(req));
    autoReload && (await reloadTodos());
  };

  const onDelete = async (req: DeleteTODORequest) => {
    await callTodoApi(apiOrigin, (api) => api.deleteTODO(req));
    autoReload && (await reloadTodos());
  };

  useEffect(() => {
    setTimeout(reloadTodos);
  }, [reloadTodos, apiOrigin]);

  const sortedItems = useMemo(
    () => state?.items.sort((a, b) => date.Ord.compare(a.updatedAt, b.updatedAt)).reverse() || [],
    [state],
  );

  return {
    onCreate,
    onUpdate,
    onDelete,
    onReload: reloadTodos,
    items: sortedItems,
    isLoading: !state,
  };
}
