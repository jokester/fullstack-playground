/**
 * @generated SignedSource<<6b4d276e3e6dd6abb17fe8147e434627>>
 * @lightSyntaxTransform
 * @nogrep
 */

/* tslint:disable */
/* eslint-disable */
// @ts-nocheck

import { ConcreteRequest, Query } from 'relay-runtime';
export type graphTodoApiListTodoQuery$variables = {};
export type graphTodoApiListTodoQuery$data = {
  readonly todos: ReadonlyArray<{
    readonly created_at: Date;
    readonly desc: string;
    readonly finished_at: Date | null;
    readonly title: string;
    readonly todo_id: number;
    readonly updated_at: Date;
  }>;
};
export type graphTodoApiListTodoQuery = {
  response: graphTodoApiListTodoQuery$data;
  variables: graphTodoApiListTodoQuery$variables;
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
    "name": "graphTodoApiListTodoQuery",
    "selections": (v0/*: any*/),
    "type": "query_root",
    "abstractKey": null
  },
  "kind": "Request",
  "operation": {
    "argumentDefinitions": [],
    "kind": "Operation",
    "name": "graphTodoApiListTodoQuery",
    "selections": (v0/*: any*/)
  },
  "params": {
    "cacheID": "8893981886a5ab571c62118acb65d467",
    "id": null,
    "metadata": {},
    "name": "graphTodoApiListTodoQuery",
    "operationKind": "query",
    "text": "query graphTodoApiListTodoQuery {\n  todos {\n    todo_id\n    title\n    desc\n    finished_at\n    created_at\n    updated_at\n  }\n}\n"
  }
};
})();

(node as any).hash = "5e2bdbc0dd4ef5edc8ee432deb13770c";

export default node;
