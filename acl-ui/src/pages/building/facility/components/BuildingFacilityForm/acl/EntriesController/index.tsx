import { useCallback, useState } from "react";
import { Card, Form, Stack } from "react-bootstrap";
import { Control, useFieldArray, UseFormRegister } from "react-hook-form";
import { AclEntry, SaveBuildingFacility } from "src/hooks/api/building-facility";
import EntryControl from "./EntryControl";
import FetchParentEntries from "./FetchParentEntries";
import InheritingEntriesCard from "./InheritingEntriesCard";
import SidSelect from "./SidSelect";

type Props = {
  isPending: boolean;
  control: Control<SaveBuildingFacility>;
  register: UseFormRegister<SaveBuildingFacility>;
  isInheriting?: boolean;
};

function EntriesController({ isPending, control, register, isInheriting }: Props) {
  const { fields, prepend, update, remove } = useFieldArray({ name: "acl.entries", control });
  const [isFetchingParentEntries, setFetchingParentEntries] = useState(false);
  const onSelect = useCallback(
    (sid: string, label: string) => {
      prepend({ sid, label });
    },
    [prepend],
  );
  const onChangeEntry = useCallback(
    (index: number, entry: Partial<AclEntry>) => {
      update(index, entry as AclEntry);
    },
    [update],
  );
  const onDeleteEntry = useCallback(
    (index: number) => {
      remove(index);
    },
    [remove],
  );
  const onFetchedParentEntries = useCallback(
    (entries: AclEntry[]) => {
      entries.forEach((entry) => prepend(entry));
    },
    [prepend],
  );

  return (
    <Form.Group className="mt-3" controlId="acl.entries">
      <Form.Label>
        권한
        <FetchParentEntries isPending={isPending} onFetching={setFetchingParentEntries} onFetched={onFetchedParentEntries} />
      </Form.Label>
      <SidSelect isPending={isPending || isFetchingParentEntries} onSelect={onSelect} />
      {fields.length > 0 && (
        <Card className="mt-3">
          <Card.Header>권한 부여 목록</Card.Header>
          <Card.Body>
            <Stack gap={1}>
              {fields.map((field, index) => (
                <EntryControl
                  key={index}
                  isPending={isPending || isFetchingParentEntries}
                  entry={field}
                  onChange={(entry) => onChangeEntry(index, entry)}
                  onDelete={() => onDeleteEntry(index)}
                />
              ))}
            </Stack>
          </Card.Body>
        </Card>
      )}
      {isInheriting && <InheritingEntriesCard />}
    </Form.Group>
  );
}

export default EntriesController;
