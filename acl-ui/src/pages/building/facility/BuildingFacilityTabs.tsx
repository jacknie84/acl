import { useCallback, useContext } from "react";
import { Tab, Tabs } from "react-bootstrap";
import { Outlet, useMatch, useNavigate, useSearchParams } from "react-router-dom";
import BuildingContext from "../contexts/BuildingContext";

function BuildingFacilityTabs() {
  const match = useMatch("/buildings/:buildingId/tabs/facility/tabs/:tabKey/*");
  const { tabKey = "list" } = match?.params ?? {};
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const onSelect = useCallback((key: string | null) => key && navigate(`${key}?${searchParams}`), [navigate, searchParams]);
  const { facilityPath } = useContext(BuildingContext);

  return (
    <Tabs activeKey={tabKey} onSelect={onSelect} variant="pills">
      <Tab eventKey="list" title="하위 시설 목록">
        <Outlet />
      </Tab>
      {facilityPath.length > 0 && (
        <Tab eventKey="detail" title="상세 정보">
          <Outlet />
        </Tab>
      )}
    </Tabs>
  );
}

export default BuildingFacilityTabs;
