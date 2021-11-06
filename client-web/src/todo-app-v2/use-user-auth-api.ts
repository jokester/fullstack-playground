import type { CredApi } from './use-user-cred';
import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { useMemo, useState } from 'react';
import { callFetchApiClient } from './use-fetch-api';

export function useUserAuthApi(cred: CredApi) {
  const [withLock, lockDepth] = useConcurrencyControl(1);
  const [userGeneration, setUserGeneration] = useState(0);

  /**
   * dummy request to create new user without conflict
   */
  const newUserRequest = useMemo(
    () => ({
      email: `user-${Date.now()}@example.com`,
      initialPass: `user-${Date.now()}`,
    }),
    [userGeneration],
  );

  const onCreateUser = () =>
    withLock(async () => {
      const res = await callFetchApiClient((api) =>
        api.postUserTodoApiUsers({
          createUserRequest: {
            ...newUserRequest,
            profile: { nickname: `nick-${Date.now()}` },
          },
        }),
      );

      console.debug('created user', res);
      return res;
    });

  const onLogin = () =>
    withLock(async () => {
      const res = await callFetchApiClient((api) =>
        api.postUserTodoApiAuthLogin({
          loginRequest: {
            email: newUserRequest.email,
            password: newUserRequest.initialPass,
          },
        }),
      );
      console.debug('logged in', res);
      cred.onRefreshed(res);
      return res;
    });

  const onLogout = () =>
    withLock(async () => {
      cred.onClear();
      setUserGeneration((_) => _ + 1);
    });

  return { onLogin, onLogout, onCreateUser, lockDepth } as const;
}
