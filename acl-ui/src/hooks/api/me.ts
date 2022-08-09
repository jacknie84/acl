import useApiOperation from "./api-operation";

export function useGetMeApi() {
  return useApiOperation<void, Me>((exchange) => exchange("/me"));
}

export function usePutMeApi() {
  return useApiOperation<PutMe, void>((exchange, me) => exchange("/me", { method: "put", body: JSON.stringify(me) }));
}

export type Me = { email: string; lastModifiedDate: string };
export type PutMe = { orgPassword: string; newPassword: string };
