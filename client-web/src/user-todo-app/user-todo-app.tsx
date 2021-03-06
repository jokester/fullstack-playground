import React, { FC } from 'react';
import { CredApi, useCredStorage } from './use-user-cred';
import { useUserAuthApi } from './use-user-auth-api';
import { Button } from '@chakra-ui/react';

export const UserTodoApp: FC = () => {
  const cred = useCredStorage();

  return (
    <div>
      <UserPanel cred={cred} />
      <TodoPanel cred={cred} />
    </div>
  );
};

const UserPanel: FC<{ cred: CredApi }> = (props) => {
  const authApi = useUserAuthApi(props.cred);
  const me = props.cred.getCurrent();
  return (
    <div>
      <div suppressHydrationWarning>
        {me ? (
          <p>
            logged in as user: id={me.userId} / profile = {JSON.stringify(me.userProfile)}
          </p>
        ) : (
          'not authed'
        )}
      </div>

      <Button onClick={authApi.onCreateUser} isLoading={!!authApi.lockDepth} isDisabled={!!me}>
        create dummy user
      </Button>
      <Button onClick={authApi.onLogin} isLoading={!!authApi.lockDepth} isDisabled={!!me}>
        login as dummy user
      </Button>
      <Button onClick={authApi.onLogout} isLoading={!!authApi.lockDepth} isDisabled={!me}>
        logout
      </Button>
    </div>
  );
};

const TodoPanel: FC<{ cred: CredApi }> = () => {
  return null;
};
