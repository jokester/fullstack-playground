import React, { useState } from 'react';
import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { Button, List, ListItem } from '@chakra-ui/react';
import { useMounted } from '@jokester/ts-commonutil/lib/react/hook/use-mounted';
import { LoginResponse } from '../generated/openapi-rx';
import { PreJson } from '../dummy/pre-json';
import useConstant from 'use-constant';
import { callFetchApiClient, DEBUG_saveAuth } from './use-fetch-api';

function useUserTodoApi() {
  const [withLock, lockDepth] = useConcurrencyControl();
  const [authed, setAuthed] = useState<null | LoginResponse>(null);

  const userEmail = useConstant(() => `user-${Date.now()}`);

  const onCreateUser = () =>
    withLock(async () => {
      const x = await callFetchApiClient((api) =>
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
      const x = await callFetchApiClient((api) =>
        api.postUserTodoApiAuthLogin({
          loginRequest: {
            email,
            password,
          },
        }),
      );
      setAuthed(x);
    });

  const onDummyLogin = () => onLogin(userEmail, 'pass');

  const onAuth = () =>
    withLock(async () => {
      const createdUser = await callFetchApiClient((api) =>
        api.postUserTodoApiUsers({
          createUserRequest: {
            email: userEmail,
            initialPass: 'pass',
            profile: {},
          },
        }),
      );
      const logined = await callFetchApiClient((api) =>
        api.postUserTodoApiAuthLogin({
          loginRequest: {
            email: userEmail,
            password: 'pass',
          },
        }),
      );
      DEBUG_saveAuth(logined);
      const issued = await callFetchApiClient((api) => api.postUserTodoApiAuthRefreshToken(), {
        withRefreshToken: true,
      });
    });

  return { authed, onCreateUser, onDummyLogin, onAuth };
}

export const FetchUserTodoList: React.FC<{ revision?: number; onMutated?(): void }> = (props, ref) => {
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
