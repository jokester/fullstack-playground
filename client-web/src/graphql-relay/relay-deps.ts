import { Environment, Network, Observable, RecordSource, Store } from 'relay-runtime';
import { SubscriptionClient, Observable as WsObservable } from 'subscriptions-transport-ws';
import { inServer } from '../config/build-env';

export function createRelayEnv(graphqlEndpoint: string): Environment {
  const store = new Store(new RecordSource());
  const subscriptionClient: SubscriptionClient = inServer
    ? (null as any)
    : new SubscriptionClient(graphqlEndpoint.replace(/^http/i, 'ws'), { reconnect: true });
  const network = Network.create(
    // fetch
    (operation, variables) => {
      return fetch(graphqlEndpoint, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
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
    (request, variables) => {
      const subscribeObservable = subscriptionClient.request({
        query: request.text!,
        operationName: request.name,
        variables,
      });
      // subscriptions-transport-ws observable type to Relay's    return Observable.from(subscribeObservable);}
      return toRelayObservable(subscribeObservable);
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
