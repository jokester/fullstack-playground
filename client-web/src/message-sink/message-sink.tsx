import { FC, useEffect, useMemo, useRef, useState } from 'react';
import { Box, Button, Input } from '@chakra-ui/react';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { MessageSinkApi } from './message-sink-api';

const API_ROOT = 'http://127.0.0.1:8082';

export const MessageSinkPicker: FC<{ onNameSet?(name: string): void }> = (props) => {
  const [sinkName, setSinkName] = useState<string>('');

  return (
    <Box display="flex">
      <Input
        placeholder="Sink name (any string except whitespace)"
        value={sinkName}
        onChange={(ev) => setSinkName(ev.target.value.trim())}
      />

      <Button className="ml-2" disabled={!sinkName} onClick={() => props.onNameSet?.(sinkName)}>
        GO
      </Button>
    </Box>
  );
};

export const MessageSinkDemo: FC<{ onClose?(): void; sinkName: string }> = (props) => {
  const api = useMemo(() => new MessageSinkApi(API_ROOT, props.sinkName), [props.sinkName]);

  return (
    <div className="space-y-4">
      <hr />
      <MessageSinkHttpDemo api={api} />
      <hr />
    </div>
  );
};

const MessageSinkHttpDemo: FC<{ api: MessageSinkApi }> = (props) => {
  const reqCount = useRef(0);
  const [msgDraft, setMsgDraft] = useState<string>('');

  const onGet = () => {
    const reqTag = `req #${++reqCount.current}`;
    console.debug(reqTag, 'onGet start');
    props.api.getMessage().then((res) => {
      console.debug(reqTag, 'onGet complete', res);
    });
  };

  const onWait = () => {
    const reqTag = `req #${++reqCount.current}`;
    console.debug(reqTag, 'onWait start');
    props.api.waitMessage().then((res) => {
      console.debug(reqTag, 'onWait complete', res);
    });
  };

  const onPost = () => {
    if (!msgDraft) return;
    setMsgDraft('');
    const reqTag = `req #${++reqCount.current}`;

    console.debug(reqTag, 'onPost start');
    props.api.postMessage(msgDraft).then((res) => {
      console.debug(reqTag, 'onPost complete', res);
    });
  };

  return (
    <div className="space-y-4">
      <div>
        HTTP API
        <span className="text-sm ml-2">caution: too many concurrent requests may hit your browser limit</span>
      </div>
      <div>
        <Button onClick={onGet}>GET /message-sink/{'${NAME}'}</Button>
      </div>
      <div>
        <Button onClick={onWait}>GET /message-sink/{'${NAME}'}/wait</Button>
      </div>
      <div>
        <Input value={msgDraft} placeholder="message text" onChange={(ev) => setMsgDraft(ev.target.value)} />
        <Button disabled={!msgDraft} onClick={onPost}>
          POST /message-sink/NAME
        </Button>
      </div>
    </div>
  );
};

const MessageSinkWsDemo: FC<{ wsUrl: string }> = (props) => {
  interface SocketEntities {
    subject: WebSocketSubject<string>;
    isOpen: boolean;
  }

  const [s, setS] = useState<null | SocketEntities>(null);

  useEffect(() => {
    setS(null);
    const subject = webSocket<string>({
      url: props.wsUrl,
      serializer: (value) => value,
      deserializer: (e) => e.data as string,
    });
    // TODO: xxx
  }, [props.wsUrl]);

  return null;
};
