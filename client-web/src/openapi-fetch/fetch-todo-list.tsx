import React, { useState } from 'react';
import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { Button, List, ListItem } from '@chakra-ui/react';
import { useMounted } from '@jokester/ts-commonutil/lib/react/hook/use-mounted';
import { LoginResponse } from '../generated/openapi-rx';
import { callRxApiClient } from '../openapi-rx/use-rx-api';
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

  return { authed, onCreateUser, onDummyLogin };
}

export const FetchTodoList: React.FC<{ revision?: number; onMutated?(): void }> = (props, ref) => {
  const { authed, onCreateUser, onDummyLogin } = useUserTodoApi();

  return (
    <div>
      <div>
        <Button onClick={onCreateUser}>create user</Button>
        <Button onClick={onDummyLogin}>login()</Button>
      </div>
      <div>
        <div>
          authed: <PreJson value={authed} />
        </div>
      </div>
    </div>
  );
};
