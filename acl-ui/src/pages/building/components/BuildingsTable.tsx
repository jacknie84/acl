import { Table } from "react-bootstrap";
import { generatePath, Link } from "react-router-dom";
import { BuildingSummary } from "../types";

type Props = { buildings: BuildingSummary[] };

function BuildingsTable({ buildings }: Props) {
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
        {buildings.map(({ id, name, lastModifiedDate }) => (
          <tr key={id}>
            <td>{id}</td>
            <td>
              <Link to={generatePath("/buildings/:id/tabs/form", { id: `${id}` })}>{name}</Link>
            </td>
            <td>{lastModifiedDate}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}

export default BuildingsTable;
