import type { DefaultApi, LoginResponse } from '../generated/openapi-rx';
import type { Observable } from 'rxjs';
import { either, option } from 'fp-ts';
import { callRxApiClient } from '../openapi-rx/use-rx-api';
import { useLocalStorage } from 'react-use';
import { useState } from 'react';

export interface CredApi {
  getCurrent(): null | LoginResponse;
  onRefreshed(res: LoginResponse): void;
  onClear(): void;
}

export async function callApiWithCred<T>(
  cred: CredApi,
  task: (api: DefaultApi) => Observable<T>,
): Promise<{ refreshed: option.Option<LoginResponse>; res: either.Either<unknown, T> }> {
  try {
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

export function useCredStorage(): CredApi {
  const [revivedCred, saveCred, clearCred] = useLocalStorage<null | LoginResponse>('___user_todo_api_cred');
  const [cred, setCred] = useState(revivedCred || null);

  return {
    getCurrent: () => cred,
    onRefreshed: (v) => {
      setCred(v);
      saveCred(v);
    },
    onClear: () => {
      clearCred();
      setCred(null);
    },
  };
}
