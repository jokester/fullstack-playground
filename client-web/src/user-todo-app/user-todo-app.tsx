import React, { FC } from 'react';
import { CredApi, useCredStorage } from './use-user-cred';
import { useUserAuthApi } from './use-user-auth-api';
import { Button } from '@chakra-ui/react';
import { useUserTodoApi } from './use-user-todo-api';
import { PreJson } from '../dummy/pre-json';

export const UserTodoApp: FC = () => {
  const cred = useCredStorage();

  return (
    <div>
      <UserPanel cred={cred} />
      <TodoPanelOpenApi cred={cred} />
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

const TodoPanel: FC<{ cred: CredApi }> = ({ cred }) => {
  const todoApi = useUserTodoApi(cred);
  return null;
};

const TodoPanelOpenApi: FC<{ cred: CredApi }> = ({ cred }) => {
  const todoApi = useUserTodoApi(cred);

  return (
    todoApi.todos && (
      <div>
        <ul>
          {todoApi.todos.map((todo) => (
            <li key={todo.userId}>
              <PreJson value={todo} />
              <Button isLoading={todoApi.lockDepth > 0} onClick={() => todoApi.deleteTodo(todo)}>
                delete
              </Button>
            </li>
          ))}
        </ul>

        <Button isLoading={todoApi.lockDepth > 0} onClick={() => todoApi.createTodo('title', 'desc')}>
          create
        </Button>
      </div>
    )
  );
};
