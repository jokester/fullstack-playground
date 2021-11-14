import { DeleteTODORequest, Todo, CreateTODORequest, UpdateTODORequest } from './generated';
import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { callTodoApi } from './todo-http-api';
import { Button, Checkbox } from '@chakra-ui/react';
import { array, date, ord } from 'fp-ts';

export const TodoAppV1: React.FC<{ apiOrigin: string }> = (props) => {
  const [state, setState] = useState<null | { mode: 'list'; items: Todo[] }>(null);

  const reloadTodos = useCallback(async () => {
    setState(null);
    const items = await callTodoApi(props.apiOrigin, (api) => api.listTODO());
    setState({ mode: 'list', items: items.todos ?? [] });
  }, [props.apiOrigin]);

  const onCreate = async (req: CreateTODORequest) => {
    const created = await callTodoApi(props.apiOrigin, (api) => api.createTODO(req));
    // setState((_state) => (_state?.mode === 'list' ? { mode: 'list', items: [..._state.items, created] } : null));
    await reloadTodos();
  };

  const onUpdate = async (req: UpdateTODORequest) => {
    const updated = await callTodoApi(props.apiOrigin, (api) => api.updateTODO(req));
    await reloadTodos();
  };

  const onDelete = async (req: DeleteTODORequest) => {
    await callTodoApi(props.apiOrigin, (api) => api.deleteTODO(req));
    await reloadTodos();
  };

  useEffect(() => {
    setTimeout(reloadTodos);
  }, [reloadTodos, props.apiOrigin]);

  const sortedItems = useMemo(
    () => state?.items.sort((a, b) => date.Ord.compare(a.updatedAt, b.updatedAt)).reverse() || [],
    [state],
  );

  return (
    <div>
      <TodoDraft onCreate={onCreate} />
      <hr className="my-1" />
      <div className="text-center mb-1">
        <Button isDisabled={!state} onClick={reloadTodos} backgroundColor="blue.500" color="white" width="md">
          Reload
        </Button>
      </div>
      <TodoList items={sortedItems} onUpdate={onUpdate} onDelete={onDelete} onReload={reloadTodos} />
    </div>
  );
};

const TodoList: React.FC<{
  items: Todo[];
  onUpdate?(req: UpdateTODORequest): void;
  onDelete?(req: DeleteTODORequest): void;
  onReload?(): void;
}> = (props) => (
  <ul className="space-y-2">
    {props.items.map((item) => (
      <li key={item.id}>
        <TodoShow item={item} onDelete={props.onDelete} onUpdate={props.onUpdate} />
        <hr className="mt-1" />
      </li>
    ))}
  </ul>
);

const TodoDraft: React.FC<{ onCreate?(req: CreateTODORequest): void }> = (props) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const onCreate = () => {
    props.onCreate?.({ createTodoIntent: { title, desc: description } });
    setTitle('');
    setDescription('');
  };
  const isFilled = title.length > 0 && description.length > 0;
  return (
    <div>
      <div className="flex items-baseline ">
        <label className="w-24">Title</label>
        <input value={title} onChange={(ev) => setTitle(ev.target.value)} className="flex-1 border px-1" />
      </div>
      <div className="flex mt-1 items-baseline">
        <label className="w-24">Description</label>
        <input value={description} onChange={(ev) => setDescription(ev.target.value)} className="flex-1 border px-1" />
      </div>
      <div className="mt-1 text-right">
        <Button isDisabled={!isFilled} onClick={onCreate} backgroundColor="blue.500" color="white" size="sm">
          Create
        </Button>
      </div>
    </div>
  );
};

const TodoShow: React.FC<{
  item: Todo;
  onUpdate?(req: UpdateTODORequest): void;
  onDelete?(req: DeleteTODORequest): void;
}> = (props) => {
  const [title, setTitle] = useState(props.item.title);
  const [desc, setDesc] = useState(props.item.desc);
  const [finished, setFinished] = useState(props.item.finished);
  const onSave = () => {
    props.onUpdate?.({ todoId: props.item.id, todo: { ...props.item, title, desc, finished } });
  };
  const onDelete = () => {
    props.onDelete?.({ todoId: props.item.id });
  };
  const isDirty = title !== props.item.title || desc !== props.item.desc || finished !== props.item.finished;

  return (
    <div>
      <div className="flex items-baseline ">
        <label className="w-24">Title</label>
        <input value={title} onChange={(ev) => setTitle(ev.target.value)} className="flex-1 border px-1" />
      </div>
      <div className="flex mt-1 items-baseline">
        <label className="w-24">Description</label>
        <input value={desc} onChange={(ev) => setDesc(ev.target.value)} className="flex-1 border px-1" />
      </div>
      <div className="flex mt-1 items-center">
        <label className="w-24">Finished</label>
        <Checkbox isChecked={finished} onChange={(ev) => setFinished(ev.target.checked)} className="" />
        <span className="flex-1" />
        <Button isDisabled={!isDirty} onClick={onSave} backgroundColor="blue.500" color="white" size="sm">
          Update
        </Button>
        <Button onClick={onDelete} backgroundColor="yellow.500" color="white" size="sm" className="ml-4">
          Delete
        </Button>
      </div>
    </div>
  );
};
