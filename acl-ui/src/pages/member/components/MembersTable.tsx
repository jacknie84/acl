import { Table } from "react-bootstrap";
import { generatePath, Link } from "react-router-dom";
import { MemberSummary } from "../types";

type Props = { members: MemberSummary[] };

function MembersTable({ members }: Props) {
  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>#</th>
          <th>이메일</th>
          <th>수정일시</th>
        </tr>
      </thead>
      <tbody>
        {members.map(({ id, email, lastModifiedDate }) => (
          <tr key={id}>
            <td>{id}</td>
            <td>
              <Link to={generatePath("/members/:id/form", { id: `${id}` })}>{email}</Link>
            </td>
            <td>{lastModifiedDate}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}

export default MembersTable;
