import { either, option } from 'fp-ts';
import { useLocalStorage } from 'react-use';
import { useState } from 'react';
import { pipe } from 'fp-ts/function';
import { never } from 'fp-ts/Task';
import { callFetchApiClient } from './use-fetch-api';
import type { DefaultApi, LoginResponse } from './generated';

export interface CredApi {
  getCurrent(): null | LoginResponse;
  onRefreshed(res: LoginResponse): void;
  callApi<T>(task: (api: DefaultApi, currentUser: null | LoginResponse) => Promise<T>): Promise<T>;
  callApiWithCred<T>(task: (api: DefaultApi, currentUser: LoginResponse) => Promise<T>): Promise<T>;
  onClear(): void;
}

async function _callApiWithCred<T>(
  cred: CredApi,
  task: (api: DefaultApi) => Promise<T>,
): Promise<{ refreshed: option.Option<LoginResponse>; res: either.Either<unknown, T> }> {
  try {
    console.debug('using cred', cred.getCurrent());
    const res: either.Either<unknown, T> = await callFetchApiClient<T>(task, {
      accessToken: cred.getCurrent()?.accessToken.value || undefined,
    }).then(either.right, either.left);

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
  const [revivedCred, saveCred, clearCred] = useLocalStorage<null | LoginResponse>('___user_todo_api_cred');
  const [cred, setCred] = useState(revivedCred || null);

  const credApi: CredApi = {
    getCurrent: () => cred,
    onRefreshed: (v) => {
      setCred(v);
      saveCred(v);
    },
    callApi<T>(task: (api: DefaultApi, currentUser: LoginResponse | null) => Promise<T>): Promise<T> {
      return _callApiWithCred(credApi, (api) => task(api, cred)).then((e) => getRightOrThrow(e.res) as T);
    },
    callApiWithCred<T>(task: (api: DefaultApi, currentUser: LoginResponse) => Promise<T>): Promise<T> {
      if (cred) {
        return _callApiWithCred(credApi, (api) => task(api, cred)).then((e) => getRightOrThrow(e.res) as T);
      } else {
        /* FIXME */
        return never();
      }
    },
    onClear: () => {
      clearCred();
      setCred(null);
    },
  };
  return credApi;
}
