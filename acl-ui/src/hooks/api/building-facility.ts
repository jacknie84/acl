import { useCallback } from "react";
import useApiOperation, { Exchanger } from "./api-operation";
import { Page, PageRequestParams, parameterizeObject, parameterizePageable } from "./pagination";

export function useGetBuildingFacilitiesApi(buildingId?: number | string) {
  const operation = useCallback(
    (exchange: Exchanger<Page<BuildingFacility>>, { filter, pageable }: PageRequestParams<BuildingFacilitiesFilter>) => {
      const filterPairs = parameterizeObject(filter);
      const pageablePairs = parameterizePageable(pageable);
      const init = [...filterPairs, ...pageablePairs];
      const params = new URLSearchParams(init);
      return exchange(`/buildings/${buildingId}/facilities?${params}`);
    },
    [buildingId],
  );
  return useApiOperation<PageRequestParams<BuildingFacilitiesFilter>, Page<BuildingFacility>>(operation);
}

export function usePostBuildingFacilityApi(buildingId: number | string) {
  return useApiOperation<SaveBuildingFacility, void>((exchange, member) =>
    exchange(`/buildings/${buildingId}/facilities`, { method: "post", body: JSON.stringify(member) }),
  );
}

export function useGetBuildingFacilityApi(buildingId: number | string, id: number | string) {
  return useApiOperation<void, BuildingFacility>((exchange) => exchange(`/buildings/${buildingId}/facilities/${id}`));
}

export function usePutBuildingFacilityApi(buildingId: number | string, id: number | string) {
  return useApiOperation<SaveBuildingFacility, void>((exchange, member) =>
    exchange(`/buildings/${buildingId}/facilities/${id}`, { method: "put", body: JSON.stringify(member) }),
  );
}

export function useDeleteBuildingFacilityApi(buildingId: number | string, id: number | string) {
  return useApiOperation<void, void>((exchange) => exchange(`/buildings/${buildingId}/facilities/${id}`, { method: "delete" }));
}

export type BuildingFacility = {
  id: string | number;
  name: string;
  buildingId: string | number;
  parentId?: string | number;
  lastModifiedDate: string;
};

export type BuildingFacilitiesFilter = {
  search?: string;
  parentId?: string | number;
  nullParentId?: boolean;
  ids?: (string | number)[];
};

export type SaveBuildingFacility = {
  id?: string | number;
  name?: string;
  parentId?: string | number;
  acl?: { ownerMemberId?: number | string; inheriting?: boolean; entries?: AclEntry[] };
};

export type AclEntry = {
  sid: string;
  label: string;
  read?: boolean;
  write?: boolean;
  create?: boolean;
  remove?: boolean;
  admin?: boolean;
};
