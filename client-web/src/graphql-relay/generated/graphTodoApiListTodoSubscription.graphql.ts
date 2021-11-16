/* tslint:disable */
/* eslint-disable */
// @ts-nocheck

import { ConcreteRequest } from "relay-runtime";

export type graphTodoApiListTodoSubscriptionVariables = {};
export type graphTodoApiListTodoSubscriptionResponse = {
    readonly todos: ReadonlyArray<{
        readonly todo_id: number;
        readonly title: string;
        readonly desc: string;
        readonly finished_at: Date | null;
        readonly created_at: Date;
        readonly updated_at: Date;
    }>;
};
export type graphTodoApiListTodoSubscription = {
    readonly response: graphTodoApiListTodoSubscriptionResponse;
    readonly variables: graphTodoApiListTodoSubscriptionVariables;
};



/*
subscription graphTodoApiListTodoSubscription {
  todos {
    todo_id
    title
    desc
    finished_at
    created_at
    updated_at
  }
}
*/

const node: ConcreteRequest = (function(){
var v0 = [
  {
    "alias": null,
    "args": null,
    "concreteType": "todos",
    "kind": "LinkedField",
    "name": "todos",
    "plural": true,
    "selections": [
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "todo_id",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "title",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "desc",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "finished_at",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "created_at",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "updated_at",
        "storageKey": null
      }
    ],
    "storageKey": null
  }
];
return {
  "fragment": {
    "argumentDefinitions": [],
    "kind": "Fragment",
    "metadata": null,
    "name": "graphTodoApiListTodoSubscription",
    "selections": (v0/*: any*/),
    "type": "subscription_root",
    "abstractKey": null
  },
  "kind": "Request",
  "operation": {
    "argumentDefinitions": [],
    "kind": "Operation",
    "name": "graphTodoApiListTodoSubscription",
    "selections": (v0/*: any*/)
  },
  "params": {
    "cacheID": "3c602f81efce2852ad91d3f48afa9505",
    "id": null,
    "metadata": {},
    "name": "graphTodoApiListTodoSubscription",
    "operationKind": "subscription",
    "text": "subscription graphTodoApiListTodoSubscription {\n  todos {\n    todo_id\n    title\n    desc\n    finished_at\n    created_at\n    updated_at\n  }\n}\n"
  }
};
})();
(node as any).hash = 'fe8cef8afb9e2702b2172a78c7bfeded';
export default node;
