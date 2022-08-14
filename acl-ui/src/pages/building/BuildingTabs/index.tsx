import { useCallback } from "react";
import { Tab, Tabs } from "react-bootstrap";
import { Outlet, useMatch, useNavigate } from "react-router-dom";

function BuildingTabs() {
  const match = useMatch("/buildings/:buildingId/tabs/:tabKey/*");
  const { tabKey = "form" } = match?.params || {};
  const navigate = useNavigate();
  const onSelect = useCallback((key: string | null) => key && navigate(key), [navigate]);

  return (
    <Tabs className="mt-3" activeKey={tabKey} onSelect={onSelect}>
      <Tab eventKey="form" title="수정">
        <Outlet />
      </Tab>
      <Tab eventKey="facility" title="시설">
        <Outlet />
      </Tab>
    </Tabs>
  );
}

export default BuildingTabs;
