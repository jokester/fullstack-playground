import React, { useEffect, useRef, useState } from 'react';
import { useRouter } from 'next/router';
import { Button, Heading, Input } from '@chakra-ui/react';
import { DefaultMeta } from '../../src/components/meta/default-meta';
import { MessageSinkApi } from '../../src/message-sink/message-sink-api';
import { useCounter, useLocalStorage } from 'react-use';
import { MessageSinkHttpDemo, MessageSinkWsDemo } from '../../src/message-sink/message-sink';

// const DEFAULT_API_ORIGIN = 'http://127.0.0.1:8082/stateless-akka-http';
const DEFAULT_API_ORIGIN = 'https://server-demo.jokester.io/stateless-akka-http';

const MessageSinkShowPage: React.FC = () => {
  const router = useRouter();
  const { sinkName } = router.query as { sinkName: string };
  const [apiOrigin, saveApiOrigin] = useLocalStorage('message-api-demo', DEFAULT_API_ORIGIN, { raw: true });
  const [api, setApi] = useState<null | MessageSinkApi>(null);
  const [epoch, epochControl] = useCounter(0);

  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (sinkName && apiOrigin) {
      setApi(new MessageSinkApi(apiOrigin, sinkName));
      epochControl.inc();
    }
  }, [apiOrigin, sinkName, epochControl]);

  return (
    <div className="max-w-screen-sm mx-auto pt-24 px-2 md:px-0">
      <DefaultMeta title={sinkName ? `${sinkName} | Message Sink DEMO` : `Message Sink Demo`} />
      <div className="h-24">
        <Heading as="h1" className="text-center " fontSize="xl">
          DEMO: message-sink API
          <span className="text-sm ml-2">(open Developer Console to see messages)</span>
        </Heading>
        <div className="mt-4 text-right">
          <Button className="ml-4" size="sm" onClick={() => router.push('/message-sink')}>
            BACK
          </Button>
        </div>
      </div>

      <div>
        <Input defaultValue={apiOrigin} placeholder="API origin (http or https)" ref={inputRef} />
        <Button
          onClick={() => {
            const newOrigin = inputRef?.current?.value ?? '';
            if (newOrigin) saveApiOrigin(newOrigin);
          }}
        >
          set API Origin
        </Button>
      </div>

      {api && (
        <div className="space-y-4" key={epoch}>
          <hr />
          <MessageSinkHttpDemo api={api} />
          <hr />
          <MessageSinkWsDemo api={api} />
        </div>
      )}
    </div>
  );
};
export default MessageSinkShowPage;
