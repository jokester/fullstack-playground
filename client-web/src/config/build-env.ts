declare const process: {
  env: {
    NEXT_DEV: boolean;
  };
};

export const inBrowser = typeof window !== 'undefined';
export const inServer = !inBrowser;

export const isDevBuild = Boolean(process.env.NEXT_DEV);

export const buildEnv = {} as const;

export type BuildEnv = typeof buildEnv;

export const defaultApiEndpoints = isDevBuild
  ? ({
      statelessOpenAPI: 'http://127.0.0.1:8080/stateless-openapi',
      statedOpenAPI: 'http://127.0.0.1:8080/stated-openapi',
      graphql: 'http://127.0.0.1:61080/v1/graphql',
    } as const)
  : ({
      statelessOpenAPI: 'https://scala-server-demo.ihate.work/stateless-openapi',
      statedOpenAPI: 'https://scala-server-demo.ihate.work/stated-openapi',
      graphql: 'https://scala-server-demo.ihate.work/v1/graphql',
    } as const);
