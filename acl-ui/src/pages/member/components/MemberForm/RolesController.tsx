import { Col, Form, Row } from "react-bootstrap";
import { Control, Controller, UseFormRegister } from "react-hook-form";
import { SaveMember } from "../../types";

type Props = { isPending: boolean; control: Control<SaveMember>; register: UseFormRegister<SaveMember> };

function RolesController({ isPending, control, register }: Props) {
  return (
    <Controller
      name="roles"
      control={control}
      render={({ field: { value = [], onChange, ...otherProps }, fieldState: { error }, formState: { isSubmitted } }) => (
        <Form.Group className="mb-3">
          <Form.Label>권한</Form.Label>
          <Row>
            <Col>
              <Form.Check
                disabled={isPending}
                type="switch"
                label="관리 사용자"
                value="ROLE_ADMIN"
                checked={value.includes("ROLE_ADMIN")}
                {...otherProps}
                onChange={(e) =>
                  onChange(e.target.checked ? [...value, e.target.value] : value.filter((role) => e.target.value !== role))
                }
              />
            </Col>
          </Row>
          {error && <Form.Control.Feedback type="invalid">{error.message}</Form.Control.Feedback>}
        </Form.Group>
      )}
    />
  );
}

export default RolesController;
