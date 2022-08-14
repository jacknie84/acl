import { useCallback, useContext } from "react";
import { Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import { BuildingFacility } from "src/hooks/api/building-facility";
import { displayDateTime } from "src/utils/format/date-time";
import BuildingFacilityContext from "../contexts/BuildingFacilityContext";

type Props = {
  facilities: BuildingFacility[];
};

function BuildingFacilitiesTable({ facilities }: Props) {
  const { facilityPath } = useContext(BuildingFacilityContext);
  const getFacilityPathParams = useCallback(
    (facility: BuildingFacility) => {
      if (facilityPath.every(Boolean)) {
        const ids = facilityPath.map(({ id }) => id);
        return new URLSearchParams({ facilityPath: JSON.stringify([...ids, facility.id]) });
      } else {
        return new URLSearchParams();
      }
    },
    [facilityPath],
  );

  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>#</th>
          <th>이름</th>
          <th>수정일시</th>
        </tr>
      </thead>
      <tbody>
        {facilities.length > 0 ? (
          facilities.map((facility, index) => (
            <tr key={index}>
              {facility && (
                <>
                  <td>{facility.id}</td>
                  <td>
                    <Link to={`?${getFacilityPathParams(facility)}`}>{facility.name}</Link>
                  </td>
                  <td>{displayDateTime(facility.lastModifiedDate)}</td>
                </>
              )}
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan={3}>검색 된 내부 시설이 없습니다.</td>
          </tr>
        )}
      </tbody>
    </Table>
  );
}

export default BuildingFacilitiesTable;
