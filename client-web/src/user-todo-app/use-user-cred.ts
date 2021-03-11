import type { DefaultApi, LoginResponse } from '../generated/openapi-rx';
import type { Observable } from 'rxjs';
import { either, option } from 'fp-ts';
import { callRxApiClient } from '../openapi-rx/use-rx-api';
import { useLocalStorage } from 'react-use';
import { useState } from 'react';
import { pipe } from 'fp-ts/function';

export interface CredApi {
  getCurrent(): null | LoginResponse;
  onRefreshed(res: LoginResponse): void;
  callApiWithCred<T>(task: (api: DefaultApi, currentUser: null | LoginResponse) => Observable<T>): Promise<T>;
  onClear(): void;
}

async function _callApiWithCred<T>(
  cred: CredApi,
  task: (api: DefaultApi) => Observable<T>,
): Promise<{ refreshed: option.Option<LoginResponse>; res: either.Either<unknown, T> }> {
  try {
    console.debug('using cred', cred.getCurrent());
    const res: either.Either<unknown, T> = await callRxApiClient<T>(task, {
      accessToken: cred.getCurrent()?.accessToken.value || undefined,
    }).then(either.left, either.right);

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
    callApiWithCred<T>(task: (api: DefaultApi, currentUser: LoginResponse | null) => Observable<T>): Promise<T> {
      return _callApiWithCred(credApi, (api) => task(api, cred)).then((e) => getRightOrThrow(e.res) as T);
    },
    onClear: () => {
      clearCred();
      setCred(null);
    },
  };
  return credApi;
}
