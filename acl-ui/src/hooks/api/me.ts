import useApiOperation from "./api-operation";

export function useGetMeApi() {
  return useApiOperation<void, Me>((exchange) => exchange("/me"));
}

export function usePatchMePasswordApi() {
  return useApiOperation<PatchMePassword, void>((exchange, me) =>
    exchange("/me/password", { method: "patch", body: JSON.stringify(me) }),
  );
}

export type Me = { email: string; lastModifiedDate: string };
export type PatchMePassword = { orgPassword: string; newPassword: string };
