import useApiOperation from "./api-operation";
import { Page, PageRequestParams, parameterizeObject, parameterizePageable } from "./pagination";

export function useGetBuildingsApi() {
  return useApiOperation<PageRequestParams<BuildingsFilter>, Page<Building>>((exchange, { filter, pageable }) => {
    const init = [...parameterizeObject(filter), ...parameterizePageable(pageable)];
    const params = new URLSearchParams(init);
    return exchange(`/buildings?${params}`);
  });
}

export function usePostBuildingApi() {
  return useApiOperation<SaveBuilding, void>((exchange, member) =>
    exchange("/buildings", { method: "post", body: JSON.stringify(member) }),
  );
}

export function useGetBuildingApi(id: number) {
  return useApiOperation<void, Building>((exchange) => exchange(`/buildings/${id}`));
}

export function usePutBuildingApi(id: number) {
  return useApiOperation<SaveBuilding, void>((exchange, member) =>
    exchange(`/buildings/${id}`, { method: "put", body: JSON.stringify(member) }),
  );
}

export function useDeleteBuildingApi(id: number) {
  return useApiOperation<void, void>((exchange) => exchange(`/buildings/${id}`, { method: "delete" }));
}

export type Building = {
  id: string | number;
  name: string;
  lastModifiedDate: string;
};

export type BuildingsFilter = {
  search?: string;
};

export type SaveBuilding = { id?: string | number; name?: string };
