/**
 * @generated SignedSource<<7747cf2a463c525b51865f90d1df8e9b>>
 * @lightSyntaxTransform
 * @nogrep
 */

/* tslint:disable */
/* eslint-disable */
// @ts-nocheck

import { ConcreteRequest, Query } from 'relay-runtime';
export type graphUserTodoApiListMyTodosQuery$variables = {
  userId: number;
};
export type graphUserTodoApiListMyTodosQuery$data = {
  readonly todos: ReadonlyArray<{
    readonly created_at: Date;
    readonly description: string;
    readonly finished_at: Date | null;
    readonly title: string;
    readonly todo_id: number;
    readonly updated_at: Date;
    readonly user_id: number;
  }>;
  readonly user: ReadonlyArray<{
    readonly updated_at: Date;
    readonly user_id: number;
  }>;
};
export type graphUserTodoApiListMyTodosQuery = {
  response: graphUserTodoApiListMyTodosQuery$data;
  variables: graphUserTodoApiListMyTodosQuery$variables;
};

const node: ConcreteRequest = (function(){
var v0 = [
  {
    "defaultValue": null,
    "kind": "LocalArgument",
    "name": "userId"
  }
],
v1 = {
  "alias": null,
  "args": null,
  "kind": "ScalarField",
  "name": "updated_at",
  "storageKey": null
},
v2 = {
  "alias": null,
  "args": null,
  "kind": "ScalarField",
  "name": "user_id",
  "storageKey": null
},
v3 = [
  {
    "alias": "user",
    "args": [
      {
        "fields": [
          {
            "fields": [
              {
                "kind": "Variable",
                "name": "_eq",
                "variableName": "userId"
              }
            ],
            "kind": "ObjectValue",
            "name": "user_id"
          }
        ],
        "kind": "ObjectValue",
        "name": "where"
      }
    ],
    "concreteType": "user_todo_users",
    "kind": "LinkedField",
    "name": "user_todo_users",
    "plural": true,
    "selections": [
      (v1/*: any*/),
      (v2/*: any*/)
    ],
    "storageKey": null
  },
  {
    "alias": "todos",
    "args": null,
    "concreteType": "user_todo_user_todos",
    "kind": "LinkedField",
    "name": "user_todo_user_todos",
    "plural": true,
    "selections": [
      (v2/*: any*/),
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
        "name": "finished_at",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "description",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "created_at",
        "storageKey": null
      },
      (v1/*: any*/)
    ],
    "storageKey": null
  }
];
return {
  "fragment": {
    "argumentDefinitions": (v0/*: any*/),
    "kind": "Fragment",
    "metadata": null,
    "name": "graphUserTodoApiListMyTodosQuery",
    "selections": (v3/*: any*/),
    "type": "query_root",
    "abstractKey": null
  },
  "kind": "Request",
  "operation": {
    "argumentDefinitions": (v0/*: any*/),
    "kind": "Operation",
    "name": "graphUserTodoApiListMyTodosQuery",
    "selections": (v3/*: any*/)
  },
  "params": {
    "cacheID": "7050681da1c3775b6302144a72116e9b",
    "id": null,
    "metadata": {},
    "name": "graphUserTodoApiListMyTodosQuery",
    "operationKind": "query",
    "text": "query graphUserTodoApiListMyTodosQuery(\n  $userId: Int!\n) {\n  user: user_todo_users(where: {user_id: {_eq: $userId}}) {\n    updated_at\n    user_id\n  }\n  todos: user_todo_user_todos {\n    user_id\n    todo_id\n    title\n    finished_at\n    description\n    created_at\n    updated_at\n  }\n}\n"
  }
};
})();

(node as any).hash = "dea6a455a16ee2aa34597c18130a3a53";

export default node;
