import React from 'react';
import { MessageSinkPicker } from '../../src/message-sink/message-sink';
import { useRouter } from 'next/router';
import { Heading } from '@chakra-ui/react';
import {DefaultMeta} from "../../src/components/meta/default-meta";

const MessageSinkIndexPage: React.FC = () => {
  const router = useRouter();
  return (
    <div className="max-w-screen-sm mx-auto pt-24">
      <DefaultMeta title="Message Sink DEMO" />
      <div className="h-24">
        <Heading as="h1" fontSize="xl" className="text-center">
          DEMO: message-sink API
        </Heading>
      </div>
      <MessageSinkPicker onNameSet={(name) => router.push(`/message-sink/${encodeURIComponent(name)}`)} />
    </div>
  );
};

export default MessageSinkIndexPage;
