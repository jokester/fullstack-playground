import { FC, useEffect, useRef, useState } from 'react';
import { Box, Button, Center, Input } from '@chakra-ui/react';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

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

export const MessageSinkDemo: FC<{ onClose?(): void; name: string }> = (props) => {
  const msgLogCount = useRef(100);

  return null;
};

export const MessageSinkWsDemo: FC<{ wsUrl: string }> = (props) => {
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
