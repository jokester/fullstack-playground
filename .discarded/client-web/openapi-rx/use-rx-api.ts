import type { ConfigurationParameters, DefaultApi, RequestArgs } from '../generated/openapi-rx';
import { inServer } from '../../src/config/build-env';
import { Never } from '@jokester/ts-commonutil/lib/concurrency/timing';
import { PromiseResult, usePromised } from '@jokester/ts-commonutil/lib/react/hook/use-promised';
import { useEffect, useState } from 'react';
import { Observable } from 'rxjs';

if (1) {
  throw new Error(
    `
    DEPRECATED: use other generator instead because openapi-rx generator does not:
    - recognize Date type
    
    `.trim(),
  );
}
async function configApi(conf: ConfigurationParameters): Promise<DefaultApi> {
  if (inServer) {
    return Never;
  }
  const { DefaultApi, Configuration } = await import('../generated/openapi-rx');
  return new DefaultApi(new Configuration({ basePath: `http://localhost:8080`, ...conf }));
}

export function callRxApiClient<T>(
  task: (api: DefaultApi) => Observable<T>,
  options?: { accessToken?: string; refreshToken?: string },
): Promise<T> {
  return configApi({
    middleware: [
      {
        pre(req: RequestArgs): RequestArgs {
          return {
            ...req,
            headers: {
              ...req.headers,
              Authorization:
                (options?.accessToken && `Bearer ${options.accessToken}`) ||
                (options?.refreshToken && `Bearer ${options.refreshToken}`) ||
                undefined,
            } as any,
          };
        },
      },
    ],
  }).then((api): Promise<T> => task(api).toPromise());
}

export function useApiResult<T>(
  task: (api: DefaultApi) => Promise<T>,
  deps?: unknown[],
): readonly [PromiseResult<T>, () => void] {
  const [resultP, setResultP] = useState<Promise<T>>(Never);
  const [count, setCounter] = useState(1);

  useEffect(
    () => {
      let effective = true;
      configApi({}).then((api) => effective && setResultP(task(api)));
      return () => {
        effective = false;
      };
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    deps ? [count, ...deps] : [count],
  );

  return [usePromised(resultP), () => setCounter((_) => _ + 1)] as const;
}
