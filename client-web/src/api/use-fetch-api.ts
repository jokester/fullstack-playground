import type { DefaultApi, LoginResponse } from '../generated/openapi-fetch';
import { inServer } from '../config/build-env';
import { Never } from '@jokester/ts-commonutil/lib/concurrency/timing';
import { PromiseResult, usePromised } from '@jokester/ts-commonutil/lib/react/hook/use-promised';
import { useEffect, useState } from 'react';

const apiP: Promise<DefaultApi> = inServer
  ? Never
  : import('../generated/openapi-fetch').then(
      (_) => new _.DefaultApi(new _.Configuration({ basePath: `http://localhost:8080` })),
    );

function readAuth(): null | LoginResponse {
  if (inServer) return null;
  try {
    return JSON.parse(localStorage.getItem('__auth') || '-');
  } catch (whatever) {
    return null;
  }
}

export function DEBUG_saveAuth(loginRes: LoginResponse): void {
  return localStorage.setItem('__auth', JSON.stringify(loginRes));
}

export function callFetchApiClient<T>(
  task: (api: DefaultApi) => Promise<T>,
  options?: { accessToken?: string },
): Promise<T> {
  return apiP
    .then(
      (api): DefaultApi =>
        api.withPreMiddleware(async ({ init, url }) => {
          return {
            url: url,
            init: {
              ...init,
              mode: 'cors',
              headers: {
                ...init.headers,
                Authorization: (options?.accessToken && `Bearer ${options.accessToken}`) || '',
              },
            },
          };
        }),
    )
    .then((api) => task(api));
}

export function useFetchApiResult<T>(
  task: (api: DefaultApi) => Promise<T>,
  deps?: unknown[],
  options?: { withAccessToken?: boolean; withRefreshToken?: boolean },
): readonly [PromiseResult<T>, () => void] {
  const [resultP, setResultP] = useState<Promise<T>>(Never);
  const [count, setCounter] = useState(1);

  useEffect(
    () => {
      let effective = true;
      apiP.then((api) => effective && setResultP(task(api)));
      return () => {
        effective = false;
      };
    },
    deps
      ? [count, options?.withAccessToken, options?.withRefreshToken, ...deps]
      : [count, options?.withAccessToken, options?.withRefreshToken],
  );

  return [usePromised(resultP), () => setCounter((_) => _ + 1)] as const;
}
