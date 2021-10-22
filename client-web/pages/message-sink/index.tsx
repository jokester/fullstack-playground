import React from 'react';
import { MessageSinkPicker } from '../../src/message-sink/message-sink';
import { useRouter } from 'next/router';
import { Heading } from '@chakra-ui/react';

const MessageSinkIndexPage: React.FC = () => {
  const router = useRouter();
  return (
    <div className="max-w-screen-sm mx-auto pt-24">
      <Heading as="h1" fontSize="lg" className="mb-12 text-center">
        DEMO: message-sink API
      </Heading>
      <MessageSinkPicker onNameSet={(name) => router.push(`/message-sink/${encodeURIComponent(name)}`)} />
    </div>
  );
};

export default MessageSinkIndexPage;
