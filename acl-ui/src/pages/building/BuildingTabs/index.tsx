import { useCallback, useEffect, useState } from "react";
import { Tab, Tabs } from "react-bootstrap";
import { Outlet, useMatch, useNavigate, useSearchParams } from "react-router-dom";
import BuildingContext, { defaultValue } from "../contexts/BuildingContext";
import facilitiesPagesJson from "../facility/facilitiesPages.json";
import { BuildingFacilitySummary } from "../facility/types";
import { BuildingSummary } from "../types";

function BuildingTabs() {
  const match = useMatch("/buildings/:buildingId/tabs/:tabKey/*");
  const { tabKey = "form", buildingId } = match?.params || {};
  const navigate = useNavigate();
  const onSelect = useCallback((key: string | null) => key && navigate(key), [navigate]);
  const building = useBuilding(buildingId);
  const facilityPath = useFacilityPath();
  const facilities = useFacilities(facilityPath);

  return (
    <BuildingContext.Provider value={{ building, facilities, facilityPath }}>
      <Tabs className="mt-3" activeKey={tabKey} onSelect={onSelect}>
        <Tab eventKey="form" title="수정">
          <Outlet />
        </Tab>
        <Tab eventKey="facility" title="시설">
          <Outlet />
        </Tab>
      </Tabs>
    </BuildingContext.Provider>
  );
}

function useBuilding(id?: number | string) {
  const [building, setBuilding] = useState<BuildingSummary>();
  const getBuildingAsync = useCallback(async (id: number | string) => {
    return { id, name: "꼬마빌딩(50억)", lastModifiedDate: new Date().toJSON() };
  }, []);

  useEffect(() => {
    if (id) {
      getBuildingAsync(id).then(setBuilding);
    }
  }, [id, getBuildingAsync]);

  return building;
}

function useFacilityPath() {
  const [facilityPath, setFacilityPath] = useState<string[]>(defaultValue.facilityPath);
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const facilityPath = (searchParams.has("facilityPath") ? JSON.parse(searchParams.get("facilityPath")!) : []) as string[];
    setFacilityPath(facilityPath);
  }, [searchParams]);

  return facilityPath;
}

function useFacilities(ids: (number | string)[]) {
  const [facilities, setFacilities] = useState<Record<string, BuildingFacilitySummary>>(defaultValue.facilities);

  useEffect(() => {
    const facilities = ids
      .filter((id) => facilitiesMap[id])
      .map((id) => facilitiesMap[id])
      .reduce((prev, curr) => ({ ...prev, [curr.id]: curr }), {});
    setFacilities(facilities);
  }, [ids]);

  return facilities;
}

const facilitiesPagesUnknown = facilitiesPagesJson as Record<string, unknown>;
const facilitiesPages = facilitiesPagesUnknown as Record<string, BuildingFacilitySummary[][]>;
const facilitiesMap = Object.values(facilitiesPages)
  .reduce((prev, curr) => [...prev, ...curr], [])
  .reduce((prev, curr) => [...prev, ...curr], [])
  .reduce((prev, curr) => ({ ...prev, [curr.id]: curr }), {} as Record<string, BuildingFacilitySummary>);

export default BuildingTabs;
