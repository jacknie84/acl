import { useCallback } from "react";
import useGetIdTokenAsync from "./get-id-token-async";

export type Exchanger<O> = (path: string, init?: RequestInit) => Promise<O>;
export type ApiOperation<I = unknown, O = unknown> = (exchange: Exchanger<O>, input: I) => Promise<O>;

export default function useApiOperation<I, O>(operation: ApiOperation<I, O>) {
  const getIdTokenAsync = useGetIdTokenAsync();
  return useCallback(
    async (input: I) => {
      const idToken = await getIdTokenAsync();
      const exchange: Exchanger<O> = async (path, init = {}) => {
        const method = (init.method ?? "GET").toUpperCase();
        const headers = { "Content-Type": "application/json", Authorization: `Bearer ${idToken}`, ...(init.headers ?? {}) };
        const response = await fetch(`http://localhost:8080${path}`, { ...init, method, headers });
        if (response.ok) {
          if (response.status !== 204) {
            return (await response.json()) as O;
          } else {
            return {} as O;
          }
        } else {
          throw new FetchResponseError(response, `${method} ${path} API 호출 도중 에러`);
        }
      };
      return await operation(exchange, input);
    },
    [getIdTokenAsync, operation],
  );
}

export class FetchResponseError extends Error {
  response?: Response;

  constructor(response: Response, message?: string) {
    super(message);
    this.response = response;
  }
}
