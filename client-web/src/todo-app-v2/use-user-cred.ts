import { either, option } from 'fp-ts';
import { useLocalStorage } from 'react-use';
import { useMemo, useState } from 'react';
import { pipe } from 'fp-ts/function';
import { never } from 'fp-ts/Task';
import { callFetchApiClient } from './use-fetch-api';
import type { DefaultApi, AuthSuccess } from './generated';

export interface CredApi {
  getCurrent(): null | AuthSuccess;
  onRefreshed(res: AuthSuccess): void;
  callApi<T>(task: (api: DefaultApi, currentUser: null | AuthSuccess) => Promise<T>): Promise<T>;
  callApiWithCred<T>(task: (api: DefaultApi, currentUser: AuthSuccess) => Promise<T>): Promise<T>;
  onClear(): void;
}

async function _callApiWithCred<T>(
  cred: CredApi,
  task: (api: DefaultApi) => Promise<T>,
): Promise<{ refreshed: option.Option<AuthSuccess>; res: either.Either<unknown, T> }> {
  try {
    const apiOption = {
      accessToken: cred.getCurrent()?.accessToken || undefined,
    };
    console.debug('using cred', cred.getCurrent(), apiOption);
    const res: either.Either<unknown, T> = await callFetchApiClient<T>(task, apiOption).then(either.right, either.left);

    // TODO: handle token refresh and stuff
    return {
      res,
      refreshed: option.none,
    };
  } catch (e) {
    console.error('error ', e);
    return { res: either.left(e), refreshed: option.none };
  }
}

function getRightOrThrow<T>(e: either.Either<any, T>): T {
  return pipe(
    e,
    either.getOrElse<unknown, T>((left) => {
      throw left;
    }),
  );
}

export function useCredStorage(): CredApi {
  const [revivedCred, saveCred, clearCred] = useLocalStorage<null | AuthSuccess>('___user_todo_api_auth_success');
  const [cred, setCred] = useState(revivedCred || null);

  const credApi: CredApi = useMemo(
    (): CredApi => ({
      getCurrent: () => cred,
      onRefreshed: (v) => {
        setCred(v);
        saveCred(v);
      },
      callApi<T>(task: (api: DefaultApi, currentUser: AuthSuccess | null) => Promise<T>): Promise<T> {
        return _callApiWithCred(credApi, (api) => task(api, cred)).then((e) => getRightOrThrow(e.res) as T);
      },
      callApiWithCred<T>(task: (api: DefaultApi, currentUser: AuthSuccess) => Promise<T>): Promise<T> {
        if (cred) {
          return _callApiWithCred(credApi, (api) => task(api, cred)).then((e) => getRightOrThrow(e.res) as T);
        } else {
          return Promise.reject(new Error('cred not available'));
        }
      },
      onClear: () => {
        clearCred();
        setCred(null);
      },
    }),
    [cred],
  );
  return credApi;
}
