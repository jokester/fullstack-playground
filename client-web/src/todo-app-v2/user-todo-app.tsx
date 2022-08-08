import React, { FC, useEffect, useState } from 'react';
import { useInServer } from '@jokester/ts-commonutil/lib/react/hook/use-in-server';
import { CredApi, useCredStorage } from './use-user-cred';
import { useUserAuthApi } from './use-user-auth-api';
import { Button, Checkbox } from '@chakra-ui/react';
import { useUserTodoApi } from './use-user-todo-api';
import { TodoItem } from './generated';
import { useUserTodoGqlApi } from './user-todo-gql-api';

export const UserTodoApp: FC = () => {
  const credApi = useCredStorage();
  const cred = credApi.getCurrent();
  const inServer = useInServer();

  if (inServer) {
    return null;
  }

  return (
    <div>
      <UserPanel cred={credApi} />

      <hr className="my-4" />
      {cred ? <TodoPanelOpenApi cred={credApi} /> : <div>login to see your TODOs</div>}
    </div>
  );
};

const UserPanel: FC<{ cred: CredApi }> = (props) => {
  const authApi = useUserAuthApi(props.cred);
  const me = props.cred.getCurrent();
  const gqlApi = useUserTodoGqlApi(props.cred);
  return (
    <div>
      <div>
        {me ? (
          <p>
            logged in as user: id={me.userId} / profile = {JSON.stringify(me.profile)}
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
      <Button onClick={() => gqlApi.listQuery()}>fetch own todos</Button>
      <Button onClick={() => gqlApi.listQuery(2)}>fetch todos(uid=1)</Button>
    </div>
  );
};

const TodoPanelOpenApi: FC<{ cred: CredApi }> = ({ cred }) => {
  const todoApi = useUserTodoApi(cred);

  useEffect(() => {
    if (cred.getCurrent()) {
      todoApi.reloadTodo();
    }
  }, [cred.getCurrent()?.userId]);

  return (
    todoApi.todos && (
      <div>
        <ul className="space-y-2">
          {todoApi.todos.map((todo) => (
            <li key={todo.todoId}>
              <TodoPanelItemOpenAPI todoApi={todoApi} todo={todo} />
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

const TodoPanelItemOpenAPI: React.FC<{ todoApi: ReturnType<typeof useUserTodoApi>; todo: TodoItem }> = ({
  todo,
  todoApi,
}) => {
  const [titleInput, setTitleInput] = useState(todo.title);
  const [descInput, setDescInput] = useState(todo.description);
  const [finishedAt, setFinishedAt] = useState<undefined | Date>(todo.finishedAt || undefined);
  useEffect(() => {
    setTitleInput(todo.title);
    setDescInput(todo.description);
    setFinishedAt(todo.finishedAt || undefined);
  }, [todo]);

  const finishedAtView = finishedAt?.toUTCString() || 'EMPTY';
  const dirty =
    titleInput !== todo.title || descInput !== todo.description || finishedAt?.getTime() !== todo.finishedAt?.getTime();

  return (
    <div className="p-4">
      <h4>todo id={todo.todoId}</h4>
      <div className="flex">
        <label className="w-24 inline-block font-bold ">title</label>
        <input className="flex-1" value={titleInput} onChange={(ev) => setTitleInput(ev.target.value)} />
      </div>
      <div className="flex">
        <label className="w-24 inline-block font-bold">desc</label>
        <input className="flex-1" value={descInput} onChange={(ev) => setDescInput(ev.target.value)} />
      </div>
      <div className="flex">
        <label className="w-24 inline-block font-bold flex justify-baseline">
          finished &nbsp;
          <Checkbox
            isChecked={!!finishedAt}
            onChange={(ev) => {
              if (ev.target.checked) {
                setFinishedAt(new Date());
              } else {
                setFinishedAt(undefined);
              }
            }}
          />
        </label>
        <div className="flex-1">{finishedAtView || ''}</div>
      </div>
      <div className="text-right space-x-2">
        <Button size="sm" isLoading={todoApi.lockDepth > 0} onClick={() => todoApi.deleteTodo(todo)}>
          delete
        </Button>
        <Button
          size="sm"
          isLoading={todoApi.lockDepth > 0}
          isDisabled={!dirty}
          onClick={() =>
            todoApi.updateTodo({
              ...todo,
              title: titleInput,
              description: descInput,
              finishedAt,
            })
          }
        >
          save
        </Button>
      </div>
      <hr className="mt-2" />
    </div>
  );
};
