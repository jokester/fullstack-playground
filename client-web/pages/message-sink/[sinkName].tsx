import React from 'react';
import { useRouter } from 'next/router';
import { Button, Heading } from '@chakra-ui/react';
import { MessageSinkDemo } from '../../src/message-sink/message-sink';

const MessageSinkShowPage: React.FC = () => {
  const router = useRouter();
  const { sinkName } = router.query as { sinkName: string };

  return (
    <div className="max-w-screen-sm mx-auto pt-24">
      <div className="h-24">
        <Heading as="h1" className="text-center " fontSize="xl">
          DEMO: message-sink API
        </Heading>
        <div className="text-right">
          <Button size="sm" onClick={() => router.push('/message-sink')}>
            BACK
          </Button>
        </div>
      </div>

      {Boolean(sinkName) && <MessageSinkDemo sinkName={sinkName} />}
    </div>
  );
};
export default MessageSinkShowPage;
