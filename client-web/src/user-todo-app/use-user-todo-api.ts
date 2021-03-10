import { useConcurrencyControl } from '@jokester/ts-commonutil/lib/react/hook/use-concurrency-control';
import { CredApi } from './use-user-cred';

export function useUserTodoApi(cred: CredApi) {
  const [withLock, lockDepth] = useConcurrencyControl(1);
}
