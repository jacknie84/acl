import { useEffect } from "react";
import { Form } from "react-bootstrap";
import { Control, Controller, UseFormRegister } from "react-hook-form";
import { SaveBuildingFacility } from "../../types";

type Props = { isPending: boolean; control: Control<SaveBuildingFacility>; register: UseFormRegister<SaveBuildingFacility> };

function NameController({ isPending, control, register }: Props) {
  useEffect(() => {
    register("name", {
      required: "시설 이름을 입력해 주세요.",
      maxLength: { value: 255, message: "255자를 초과 했습니다." },
    });
  }, [register]);

  return (
    <Controller
      name="name"
      control={control}
      render={({ field: { value, ...otherProps }, fieldState: { error }, formState: { isSubmitted } }) => (
        <Form.Group className="mt-3" controlId="name">
          <Form.Label>시설 이름</Form.Label>
          <Form.Control
            disabled={isPending}
            placeholder="Enter facility name"
            value={value ?? ""}
            {...otherProps}
            isValid={isSubmitted && !Boolean(error)}
            isInvalid={isSubmitted && Boolean(error)}
          />
          {error && <Form.Control.Feedback type="invalid">{error.message}</Form.Control.Feedback>}
        </Form.Group>
      )}
    />
  );
}

export default NameController;
