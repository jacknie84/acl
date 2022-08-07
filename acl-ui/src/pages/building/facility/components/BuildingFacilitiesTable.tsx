import { useMemo } from "react";
import { Table } from "react-bootstrap";
import { Link, useSearchParams } from "react-router-dom";
import { BuildingFacilitySummary } from "../types";

type Props = {
  facilities: BuildingFacilitySummary[];
};

function BuildingFacilitiesTable({ facilities }: Props) {
  const [searchParams] = useSearchParams();
  const path = useMemo(() => {
    if (searchParams.has("facilityPath")) {
      return JSON.parse(searchParams.get("facilityPath")!) as string[];
    } else {
      return [];
    }
  }, [searchParams]);

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
          facilities.map(({ id, name, lastModifiedDate }) => (
            <tr key={id}>
              <td>{id}</td>
              <td>
                <Link to={`?${new URLSearchParams({ facilityPath: JSON.stringify([...path, id]) })}`}>{name}</Link>
              </td>
              <td>{lastModifiedDate}</td>
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
