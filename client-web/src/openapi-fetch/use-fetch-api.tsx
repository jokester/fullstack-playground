import type { DefaultApi } from '../generated/openapi-fetch';
import { inServer } from '../config/build-env';
import { Never } from '@jokester/ts-commonutil/lib/concurrency/timing';
import { useEffect, useState } from 'react';
import { PromiseResult, usePromised } from '@jokester/ts-commonutil/lib/react/hook/use-promised';

const apiP: Promise<DefaultApi> = inServer
  ? Never
  : import('../generated/openapi-fetch').then(
      (_) =>
        new _.DefaultApi(
          new _.Configuration({
            basePath: `http://127.0.0.1:8080`,
          }),
        ),
    );

export function callFetchApiClient<T>(task: (api: DefaultApi) => Promise<T>): Promise<T> {
  return apiP.then(task);
}

export function useFetchApiResult<T>(
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
