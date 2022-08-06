/**
 * @generated SignedSource<<b4ad0ee91c1b6656057d41996b583c39>>
 * @relayHash 3c602f81efce2852ad91d3f48afa9505
 * @lightSyntaxTransform
 * @nogrep
 */

/* tslint:disable */
/* eslint-disable */
// @ts-nocheck

// @relayRequestID 3c602f81efce2852ad91d3f48afa9505

import { ConcreteRequest, GraphQLSubscription } from 'relay-runtime';
export type graphTodoApiListTodoSubscription$variables = {};
export type graphTodoApiListTodoSubscription$data = {
  readonly todos: ReadonlyArray<{
    readonly created_at: Date;
    readonly desc: string;
    readonly finished_at: Date | null;
    readonly title: string;
    readonly todo_id: number;
    readonly updated_at: Date;
  }>;
};
export type graphTodoApiListTodoSubscription = {
  response: graphTodoApiListTodoSubscription$data;
  variables: graphTodoApiListTodoSubscription$variables;
};

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
    "id": "3c602f81efce2852ad91d3f48afa9505",
    "metadata": {},
    "name": "graphTodoApiListTodoSubscription",
    "operationKind": "subscription",
    "text": null
  }
};
})();

(node as any).hash = "fe8cef8afb9e2702b2172a78c7bfeded";

export default node;
