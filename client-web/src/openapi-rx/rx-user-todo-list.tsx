import React, { useState } from 'react';
import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { Button, List, ListItem } from '@chakra-ui/react';
import { useMounted } from '@jokester/ts-commonutil/lib/react/hook/use-mounted';
import { LoginResponse } from '../generated/openapi-rx';
import { callRxApiClient, DEBUG_saveAuth } from './use-rx-api';
import { PreJson } from '../dummy/pre-json';
import useConstant from 'use-constant';

function useUserTodoApi() {
  const [withLock, lockDepth] = useConcurrencyControl();
  const [authed, setAuthed] = useState<null | LoginResponse>(null);

  const userEmail = useConstant(() => `user-${Date.now()}`);

  const onCreateUser = () =>
    withLock(async () => {
      const x = await callRxApiClient((api) =>
        api.postUserTodoApiUsers({
          createUserRequest: {
            email: userEmail,
            initialPass: 'pass',
            profile: {},
          },
        }),
      );

      console.debug('created user', x);
    });

  const onLogin = (email: string, password: string) =>
    withLock(async () => {
      const x = await callRxApiClient((api) => api.postUserTodoApiAuthLogin({ loginRequest: { email, password } }));
      setAuthed(x);
    });

  const onDummyLogin = () => onLogin(userEmail, 'pass');

  const onAuth = () =>
    withLock(async () => {
      const createdUser = await callRxApiClient((api) =>
        api.postUserTodoApiUsers({
          createUserRequest: {
            email: userEmail,
            initialPass: 'pass',
            profile: {},
          },
        }),
      );
      const logined = await callRxApiClient((api) =>
        api.postUserTodoApiAuthLogin({
          loginRequest: {
            email: userEmail,
            password: 'pass',
          },
        }),
      );
      DEBUG_saveAuth(logined);
      const issued = await callRxApiClient((api) => api.postUserTodoApiAuthRefreshToken(), { withRefreshToken: true });
    });

  return { authed, onCreateUser, onDummyLogin, onAuth };
}

export const RxUserTodoList: React.FC<{ revision?: number; onMutated?(): void }> = (props, ref) => {
  const { authed, onCreateUser, onDummyLogin, onAuth } = useUserTodoApi();

  return (
    <div>
      <div>
        <Button onClick={onCreateUser}>create user</Button>
        <Button onClick={onDummyLogin}>login()</Button>
        <Button onClick={onAuth}>auth()</Button>
      </div>
      <div>
        <div>
          authed: <PreJson value={authed} />
        </div>
      </div>
    </div>
  );
};
