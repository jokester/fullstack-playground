import { Environment, Network, RecordSource, Store } from 'relay-runtime';

const store = new Store(new RecordSource());

const network = Network.create((operation, variables) => {
  return fetch('http://localhost:61081/v1/graphql', {
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
});

export const relayEnv = new Environment({
  network,
  store,
});
