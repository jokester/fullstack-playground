import { DeleteTODORequest, Todo, CreateTODORequest, UpdateTODORequest } from './generated';
import React, { useCallback, useEffect, useState } from 'react';
import { callTodoApi } from './todo-api';

export const TodoAppV1: React.FC<{ apiOrigin: string }> = (props) => {
  const [state, setState] = useState<null | { mode: 'list'; items: Todo[] } | { mode: 'show'; item: Todo }>(null);

  const onList = useCallback(async () => {
    setState(null);
    const items = await callTodoApi(props.apiOrigin, (api) => api.listTODO());
    setState({ mode: 'list', items });
  }, [props.apiOrigin]);

  const onShow = async (todoId: number) => {
    setState(null);
    const item = await callTodoApi(props.apiOrigin, (api) => api.showTODO({ todoId }));
    setState({ mode: 'show', item });
  };

  const onCreate = async (req: CreateTODORequest) => {
    await callTodoApi(props.apiOrigin, (api) => api.createTODO(req));
    await onList();
  };

  const onUpdate = async (req: UpdateTODORequest) => {
    await callTodoApi(props.apiOrigin, (api) => api.updateTODO(req));
    await onList();
  };

  const onDelete = async (req: DeleteTODORequest) => {
    await callTodoApi(props.apiOrigin, (api) => api.deleteTODO(req));
    await onList();
  };

  useEffect(() => {
    setTimeout(onList);
  }, [onList, props.apiOrigin]);

  return state?.mode === 'list' ? (
    <TodoList items={state.items} onSave={onUpdate} />
  ) : state?.mode === 'show' ? null : null;
};

const TodoShow: React.FC<{
  item: Todo;
  onSave?(req: UpdateTODORequest): void;
  onDelete?(req: DeleteTODORequest): void;
}> = (props) => {
  return (
    <div>
      <label>Title</label>
      <h5>{props.item.title}</h5>
    </div>
  );
};

const TodoList: React.FC<{
  items: Todo[];
  onSave?(req: UpdateTODORequest): void;
  onDelete?(req: DeleteTODORequest): void;
}> = (props) => (
  <div>
    <ul>
      {props.items.map((item) => (
        <li key={item.id}>
          <TodoShow item={item} />
        </li>
      ))}
    </ul>
  </div>
);

const TodoDraft: React.FC<{ onCreate?(req: CreateTODORequest): void }> = (props) => {
  return null;
};
