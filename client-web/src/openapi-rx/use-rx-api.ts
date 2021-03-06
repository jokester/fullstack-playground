import type { DefaultApi } from '../generated/openapi-rx';
import { inServer } from '../config/build-env';
import { Never } from '@jokester/ts-commonutil/lib/concurrency/timing';
import { PromiseResult, usePromised } from '@jokester/ts-commonutil/lib/react/hook/use-promised';
import { useEffect, useState } from 'react';
import { Observable } from 'rxjs';

const apiP: Promise<DefaultApi> = inServer
  ? Never
  : import('../generated/openapi-rx').then(
      (_) => new _.DefaultApi(new _.Configuration({ basePath: `http://localhost:8080` })),
    );

export function callRxApiClient<T>(
  task: (api: DefaultApi) => Observable<T>,
  options?: { accessToken?: string; refreshToken?: string },
): Promise<T> {
  return apiP
    .then(
      (api): DefaultApi =>
        api.withPreMiddleware([
          (req) => ({
            ...req,
            headers: {
              ...req.headers,
              Authorization:
                (options?.accessToken && `Bearer ${options.accessToken}`) ||
                (options?.refreshToken && `Bearer ${options.refreshToken}`) ||
                '',
            } as any,
          }),
        ]) as DefaultApi,
    )
    .then((api) => task(api).toPromise());
}

/**
 * fixme: handle cred stuff
 */
export function useApiResult<T>(
  task: (api: DefaultApi) => Promise<T>,
  deps?: unknown[],
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
    // eslint-disable-next-line react-hooks/exhaustive-deps
    deps ? [count, ...deps] : [count],
  );

  return [usePromised(resultP), () => setCounter((_) => _ + 1)] as const;
}
