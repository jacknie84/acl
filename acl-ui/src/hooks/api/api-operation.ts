import { useCallback } from "react";
import CodenizeError from "src/utils/validation/CodenizeError";
import useGetIdTokenAsync from "./get-id-token-async";

export type Exchanger<O> = (path: string, init?: RequestInit) => Promise<O>;
export type RequestOptions = { signal?: AbortSignal };
export type ApiOperation<I = unknown, O = unknown> = (exchange: Exchanger<O>, input: I) => Promise<O>;
export type ClientError = { code: string; message?: string };

export default function useApiOperation<I, O>(operation: ApiOperation<I, O>) {
  const getIdTokenAsync = useGetIdTokenAsync();
  const getResponseAsync = useCallback(
    async (options: RequestOptions, path: string, init: RequestInit, forceRefreshToken: boolean = false): Promise<O> => {
      const idToken = await getIdTokenAsync(forceRefreshToken);
      const request = buildRequest(idToken, options, path, init);
      const response = await fetch(request);
      if (response.ok) {
        if ([201, 204].includes(response.status)) {
          return {} as O;
        } else {
          return (await response.json()) as O;
        }
      } else {
        if (response.status === 401) {
          return getResponseAsync(options, path, init, true);
        }
        if (Math.floor(response.status / 100) === 4) {
          const contentType = response.headers.get("Content-Type");
          if (contentType?.includes("application/json")) {
            const json = await response.json();
            if ("code" in json) {
              const { code, message } = json as ClientError;
              const errorMessage = message && !message.includes(code) ? `[${code}] ${message}` : code;
              console.log(`Client Error: ${errorMessage}`);
              throw new CodenizeError(code, errorMessage);
            }
          }
        }
        const url = new URL(request.url);
        throw new FetchResponseError(response, `${request.method} ${url.pathname} API 호출 도중 에러`);
      }
    },
    [getIdTokenAsync],
  );

  return useCallback(
    async (input: I, options: RequestOptions = {}) => {
      const exchange: Exchanger<O> = async (path, init = {}) => getResponseAsync(options, path, init);
      return await operation(exchange, input);
    },
    [operation, getResponseAsync],
  );
}

function buildRequest(idToken: string, options: RequestOptions = {}, path: string, init: RequestInit = {}) {
  const method = (init.method ?? "GET").toUpperCase();
  const baseHeaders: Record<string, string> =
    method === "GET" ? { Accept: "application/json" } : { "Content-Type": "application/json" };
  const headers = { ...baseHeaders, Authorization: `Bearer ${idToken}`, ...(init.headers ?? {}) };
  return new Request(`http://localhost:8080${path}`, { ...init, method, headers, ...options });
}

export class FetchResponseError extends Error {
  response?: Response;

  constructor(response: Response, message?: string) {
    super(message);
    this.response = response;
  }
}
