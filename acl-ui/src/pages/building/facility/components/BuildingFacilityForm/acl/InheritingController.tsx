import { Form } from "react-bootstrap";
import { Control, Controller } from "react-hook-form";
import { SaveBuildingFacility } from "src/hooks/api/building-facility";

type Props = { isPending: boolean; control: Control<SaveBuildingFacility> };

function InheritingController({ isPending, control }: Props) {
  return (
    <Controller
      name="acl.inheriting"
      control={control}
      render={({ field: { value, onChange, ...otherProps }, fieldState: { error }, formState: { isSubmitted } }) => (
        <Form.Group className="mt-3" controlId="name">
          <Form.Label>권한 상속 옵션</Form.Label>
          <Form.Check
            disabled={isPending}
            type="switch"
            label="상속"
            checked={value ?? false}
            {...otherProps}
            onChange={(e) => onChange(e.target.checked)}
            isValid={isSubmitted && !Boolean(error)}
            isInvalid={isSubmitted && Boolean(error)}
          />
          {error && <Form.Control.Feedback type="invalid">{error.message}</Form.Control.Feedback>}
        </Form.Group>
      )}
    />
  );
}

export default InheritingController;
