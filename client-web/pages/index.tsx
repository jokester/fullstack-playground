import React from 'react';
import { LinkA } from '@jokester/ts-commonutil/lib/react/next/link-a';
import { FaIcon } from '@jokester/ts-commonutil/lib/react/component/font-awesome';

const IndexPage: React.FC = () => {
  return (
    <div>
      <div className="max-w-screen-sm mx-auto py-4 space-y-2">
        <p>
          Demo Client for
          <LinkA href="https://github.com/jokester/fullstack-playground">
            <span className="underline">https://github.com/jokester/fullstack-playground</span> <FaIcon icon="github" />
          </LinkA>
        </p>
        <p>
          Demo app: &nbsp;
          <LinkA href="/message-sink" className="underline">
            MessageSink
          </LinkA>
        </p>
        <p>
          Demo app: &nbsp;
          <LinkA href="/todo-list" className="underline">
            TodoList
          </LinkA>
        </p>
        <p>
          Demo app: &nbsp;
          <LinkA href="/todo-v2" className="underline">
            User Todo List
          </LinkA>
        </p>
      </div>
    </div>
  );
};

export default IndexPage;
