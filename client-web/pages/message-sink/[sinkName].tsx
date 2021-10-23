import React from 'react';
import { useRouter } from 'next/router';
import { Button, Heading } from '@chakra-ui/react';
import { MessageSinkDemo } from '../../src/message-sink/message-sink';
import { DefaultMeta } from '../../src/components/meta/default-meta';

const MessageSinkShowPage: React.FC = () => {
  const router = useRouter();
  const { sinkName } = router.query as { sinkName: string };

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

      {Boolean(sinkName) && <MessageSinkDemo sinkName={sinkName} />}
    </div>
  );
};
export default MessageSinkShowPage;
