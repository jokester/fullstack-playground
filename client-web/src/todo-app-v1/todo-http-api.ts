import type { DefaultApi } from './generated';

async function createTodoApiClient(apiOrigin: string): Promise<DefaultApi> {
  const { DefaultApi, Configuration } = await import('./generated');
  return new DefaultApi(new Configuration({ basePath: apiOrigin }));
}

export async function callTodoApi<T>(apiOrigin: string, task: (client: DefaultApi) => Promise<T>) {
  return createTodoApiClient(apiOrigin).then(task);
}
