import { useCallback } from "react";
import { Col, Form, Row } from "react-bootstrap";
import { TiDelete } from "react-icons/ti";
import { AclEntry } from "src/hooks/api/building-facility";

type Props = {
  isDisabled?: boolean;
  isPending?: boolean;
  entry: Partial<AclEntry>;
  onChange?: (entry: Partial<AclEntry>) => void;
  onDelete?: () => void;
};

type Switch = { label: string; property: keyof AclEntry };

const switches = [
  { label: "R", property: "read" },
  { label: "W", property: "write" },
  { label: "D", property: "remove" },
  { label: "C", property: "create" },
  { label: "A", property: "admin" },
] as Switch[];

function EntryControl({ isDisabled = false, isPending = false, entry, onChange = () => {}, onDelete = () => {} }: Props) {
  const onChangePermission = useCallback(
    (property: string, isChecked: boolean) => onChange({ ...entry, [property]: isChecked }),
    [onChange, entry],
  );
  const onClickDeleteIcon = useCallback(() => {
    if (isPending || isDisabled) {
      return;
    }
    onDelete();
  }, [isDisabled, isPending, onDelete]);

  return (
    <Row>
      <Col>
        <Form.Label>
          {entry.label}
          <TiDelete color={isPending || isDisabled ? "rgba(255, 0, 0, 0.3)" : "red"} onClick={onClickDeleteIcon} />
        </Form.Label>
        <Row>
          <Col>
            {switches.map(({ label, property }, index) => (
              <Form.Check
                key={index}
                disabled={isPending || isDisabled}
                inline
                type="switch"
                label={label}
                checked={Boolean(entry[property])}
                onChange={(e) => onChangePermission(property, e.currentTarget.checked)}
              />
            ))}
          </Col>
        </Row>
      </Col>
    </Row>
  );
}

export default EntryControl;
