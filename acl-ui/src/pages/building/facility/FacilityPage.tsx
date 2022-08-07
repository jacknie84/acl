import { useContext } from "react";
import { Breadcrumb, Stack } from "react-bootstrap";
import { Outlet, useSearchParams } from "react-router-dom";
import BuildingContext from "../contexts/BuildingContext";

function FacilityPage() {
  const [, setSearchParams] = useSearchParams();
  const { building, facilityPath, facilities } = useContext(BuildingContext);

  return (
    <Stack gap={3} className="mt-3">
      <Breadcrumb>
        <Breadcrumb.Item onClick={() => setSearchParams({})}>{building?.name}</Breadcrumb.Item>
        {facilityPath.map((id, index, array) => (
          <Breadcrumb.Item
            key={id}
            onClick={() => setSearchParams({ facilityPath: JSON.stringify(array.slice(0, index + 1)) })}
            active={array.length - 1 === index}
          >
            {facilities[id]?.name}
          </Breadcrumb.Item>
        ))}
      </Breadcrumb>
      <Outlet />
    </Stack>
  );
}

export default FacilityPage;
