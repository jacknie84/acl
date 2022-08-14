import { useState } from "react";
import { Card, Stack } from "react-bootstrap";
import { AclEntry } from "src/hooks/api/building-facility";
import EntryControl from "./EntryControl";

function InheritingEntriesCard() {
  const [inheritingEntries] = useState<AclEntry[]>([
    { sid: "ROLE_ADMIN", label: "총 관리자", admin: true, create: true, read: true, remove: true, write: true },
  ]);

  return (
    <>
      {inheritingEntries.length > 0 && (
        <Card bg="light" className="mt-3">
          <Card.Header>상위 시설로 부터 상속 된 권한 부여 목록</Card.Header>
          <Card.Body>
            <Stack gap={1}>
              {inheritingEntries.map((entry, index) => (
                <EntryControl key={index} isDisabled entry={entry} />
              ))}
            </Stack>
          </Card.Body>
        </Card>
      )}
    </>
  );
}

export default InheritingEntriesCard;
