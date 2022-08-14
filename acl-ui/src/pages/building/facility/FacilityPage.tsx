import { useQuery } from "@tanstack/react-query";
import { Fragment, useCallback, useMemo } from "react";
import { Breadcrumb, Stack } from "react-bootstrap";
import { Outlet, useParams, useSearchParams } from "react-router-dom";
import { useGetBuildingApi } from "src/hooks/api/building";
import { BuildingFacility, useGetBuildingFacilitiesApi } from "src/hooks/api/building-facility";
import BuildingFacilityContext from "./contexts/BuildingFacilityContext";

function FacilityPage() {
  const [, setSearchParams] = useSearchParams();
  const { buildingId } = useParams();
  const getBuildingAsync = useGetBuildingApi(buildingId!);
  const { data: building } = useQuery(["getBuilding", buildingId], () => getBuildingAsync());
  const facilityPath = useFacilityPath(buildingId!);
  const onClickBreadcrumbItem = useCallback(
    (index: number) => {
      if (facilityPath.every((facility) => Boolean(facility))) {
        const ids = facilityPath.map(({ id }) => id).slice(0, index + 1);
        setSearchParams({ facilityPath: JSON.stringify(ids) });
      }
    },
    [facilityPath, setSearchParams],
  );

  return (
    <Stack gap={3} className="mt-3">
      <Breadcrumb>
        <Breadcrumb.Item onClick={() => setSearchParams({})}>{building?.name}</Breadcrumb.Item>
        {facilityPath.map((facility, index, array) => (
          <Fragment key={index}>
            {facility && (
              <Breadcrumb.Item onClick={() => onClickBreadcrumbItem(index)} active={array.length - 1 === index}>
                {facility.name}
              </Breadcrumb.Item>
            )}
          </Fragment>
        ))}
      </Breadcrumb>
      <BuildingFacilityContext.Provider value={{ facilityPath }}>
        <Outlet />
      </BuildingFacilityContext.Provider>
    </Stack>
  );
}

function useFacilityPath(buildingId: string | number) {
  const [searchParams] = useSearchParams();
  const ids = useMemo(() => {
    if (searchParams.has("facilityPath")) {
      const jsonParam = searchParams.get("facilityPath") ?? "[]";
      return (JSON.parse(jsonParam) ?? []) as number[];
    } else {
      return [];
    }
  }, [searchParams]);
  const getFacilities = useGetBuildingFacilitiesApi(buildingId);
  const { data } = useQuery(["getFacilities", buildingId, ...ids], ({ signal }) =>
    getFacilities({ filter: { ids } }, { signal }),
  );
  return useMemo(() => {
    const facilities = (data?.content ?? []).reduce(
      (prev, curr) => ({ ...prev, [curr.id]: curr }),
      {} as Record<number | string, BuildingFacility>,
    );
    return ids.map((id) => facilities[id]);
  }, [ids, data]);
}

export default FacilityPage;
