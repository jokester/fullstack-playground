import { FC, useEffect, useMemo, useRef, useState } from 'react';
import { Box, Button, Input } from '@chakra-ui/react';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { MessageSinkApi } from './message-sink-api';
import { useCounter } from 'react-use';
import { useMounted } from '@jokester/ts-commonutil/lib/react/hook/use-mounted';

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

export const MessageSinkHttpDemo: FC<{ api: MessageSinkApi }> = (props) => {
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

export const MessageSinkWsDemo: FC<{ api: MessageSinkApi }> = (props) => {
  interface ConnState {
    subject: WebSocketSubject<string>;
    isOpen: boolean;
  }

  const [msgDraft, setMsgDraft] = useState<string>('');
  const [conn, setConn] = useState<null | ConnState>(null);

  const [connectId, connectCounter] = useCounter();
  const mounted = useMounted();

  useEffect(() => {
    setConn(null);

    const connTag = `Conn#${connectId}`;

    const patchState = (patch: Partial<ConnState>) => {
      setConn((orig) => {
        if (mounted.current && orig?.subject === subject) {
          return {
            ...orig,
            ...patch,
          };
        } else return orig;
      });
    };

    const subject = webSocket<string>({
      url: props.api.createSocketURL(),
      serializer: (value) => value,
      deserializer: (e) => e.data as string,

      openObserver: {
        next(ev) {
          console.debug(connTag, 'openObserver next', ev);
          patchState({ isOpen: true });
        },
        complete() {
          console.debug(connTag, 'openObserver complete');
        },
        error(e) {
          console.debug(connTag, 'openObserver error', e);
        },
      },

      closeObserver: {
        next(ev) {
          console.debug(connTag, 'closeObserver next', ev);
          patchState({ isOpen: false });
        },
        complete() {
          console.debug('closeObserver complete');
        },
        error(e) {
          console.debug('closeObserver error', e);
        },
      },
    });

    const incomingMsg = subject.subscribe({
      next(value: string) {
        console.debug(connTag, `incomingMsg next`, JSON.parse(value));
      },
      complete() {
        console.debug(connTag, `incomingMsg complete`);
      },
      error(err: unknown) {
        console.debug(connTag, `incomingMsg error`, err);
      },
    });

    setConn({
      subject,
      isOpen: false,
    });

    return () => {
      console.debug('closing connection');
      incomingMsg.unsubscribe(); // this closes connection
    };
  }, [connectId]);

  const onRestart = () => connectCounter.inc();

  const onClose = () => {
    if (conn?.isOpen) {
      conn.subject.complete();
    }
  };

  const onError = () => {
    if (conn?.isOpen) {
      conn.subject.error({
        /** @see https://developer.mozilla.org/en-US/docs/Web/API/CloseEvent/code */
        code: 3000,
        reason: 'client disconnect as error',
      });
    }
  };

  const onSend = () => {
    if (conn?.isOpen && msgDraft) {
      setMsgDraft('');
      conn.subject.next(msgDraft);
    }
  };

  return (
    <div className="space-y-4">
      <div>WebSocket API {conn?.isOpen ? '(connected)' : ''} </div>
      <div>
        <Button isDisabled={!conn?.isOpen} onClick={onClose}>
          CLOSE normally
        </Button>
      </div>
      <div>
        <Button isDisabled={!conn?.isOpen} onClick={onError}>
          CLOSE by error
        </Button>
      </div>
      <div>
        <Button onClick={onRestart}>RECONNECT</Button>
      </div>
      <div>
        <Input value={msgDraft} placeholder="message text" onChange={(ev) => setMsgDraft(ev.target.value)} />
        <Button disabled={!msgDraft || !conn?.isOpen} onClick={onSend}>
          send message
        </Button>
      </div>
    </div>
  );
};
