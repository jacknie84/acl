import useApiOperation from "./api-operation";
import { Page, PageRequestParams, parameterizeObject, parameterizePageable } from "./pagination";

export function useGetMemberAccountsApi() {
  return useApiOperation<PageRequestParams<MemberAccountsFilter>, Page<MemberAccount>>((exchange, { filter, pageable }) => {
    const init = [...parameterizeObject(filter), ...parameterizePageable(pageable)];
    const params = new URLSearchParams(init);
    return exchange(`/member/accounts?${params}`);
  });
}

export function usePostMemberAccountApi() {
  return useApiOperation<PostMemberAccount, void>((exchange, member) =>
    exchange("/member/accounts", { method: "post", body: JSON.stringify(member) }),
  );
}

export function useGetMemberAccountApi(id: number) {
  return useApiOperation<void, MemberAccount>((exchange) => exchange(`/member/accounts/${id}`));
}

export function usePutMemberAccountApi(id: number) {
  return useApiOperation<PostMemberAccount, void>((exchange, member) =>
    exchange(`/member/accounts/${id}`, { method: "put", body: JSON.stringify(member) }),
  );
}

export function useDeleteMemberAccountApi(id: number) {
  return useApiOperation<void, void>((exchange) => exchange(`/member/accounts/${id}`, { method: "delete" }));
}

export type MemberRole = "ROLE_USER" | "ROLE_ADMIN";

export type MemberAccount = {
  id: string | number;
  email: string;
  password?: string;
  roles: MemberRole[];
  lastModifiedDate: string;
};

export type MemberAccountsFilter = {
  search?: string;
};

export type PostMemberAccount = { email?: string; password?: string; roles?: MemberRole[] };
