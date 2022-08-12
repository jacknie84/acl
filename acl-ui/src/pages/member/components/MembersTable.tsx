import { Table } from "react-bootstrap";
import { generatePath, Link } from "react-router-dom";
import { MemberAccount } from "src/hooks/api/member-account";
import { displayDateTime } from "src/utils/format/date-time";

type Props = { members: Partial<MemberAccount>[] };

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
            <td>{displayDateTime(lastModifiedDate)}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}

export default MembersTable;
