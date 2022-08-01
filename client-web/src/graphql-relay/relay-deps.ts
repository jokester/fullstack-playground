import { Environment, Network, Observable, RecordSource, Store } from 'relay-runtime';
import { SubscriptionClient, Observable as WsObservable } from 'subscriptions-transport-ws';
import { defaultApiEndpoints, inServer } from '../config/build-env';

export function createRelayEnv(graphqlEndpoint: string, accessToken?: string, hasuraRole?: string): Environment {
  const store = new Store(new RecordSource());
  const subscriptionClient: SubscriptionClient = inServer
    ? (null as any)
    : new SubscriptionClient(graphqlEndpoint.replace(/^http/i, 'ws'), { lazy: true });
  const network = Network.create(
    // fetch
    (operation, variables) => {
      console.debug('about to fetch', operation, variables);
      return fetch(graphqlEndpoint, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          ...(accessToken && {
            Authorization: `Bearer ${accessToken}`,
            ...(hasuraRole && {
              'x-hasura-role': hasuraRole,
            }),
          }),
        },
        body: JSON.stringify({
          documentId: operation.id,
          query: operation.text,
          variables,
        }),
      }).then((response) => {
        return response.json();
      });
    },
    // subscribe
    (req) => {
      console.debug('why are you subscribing???', req);
      const wtf = subscriptionClient.request(req);
      return toRelayObservable(wtf);
    },
  );

  return new Environment({
    network,
    store,
  });
}

function toRelayObservable<T>(otherObservable: WsObservable<unknown>): Observable<T> {
  return Observable.create<T>((sink) => {
    const s = otherObservable.subscribe({
      complete: () => sink.complete(),
      error: (e) => sink.error(e),
      next: (value) => sink.next(value as T),
    });
    return () => s.unsubscribe();
  });
}

export async function fetchGraphQL(text: string, variables: Record<string, unknown> = {}) {
  console.debug('fetchGraphql', text, variables);
  // Fetch data from GitHub's GraphQL API:
  const response = await fetch(defaultApiEndpoints.graphql, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      query: text,
      variables,
    }),
  });

  // Get the response as JSON
  return await response.json();
}

export function createDemoRelayEnv() {
  const store = new Store(new RecordSource());
  console.debug('createDebugRelayEnv');
  return new Environment({
    network: Network.create((request, variables) => fetchGraphQL(request.text!, variables)),
    store,
  });
}
