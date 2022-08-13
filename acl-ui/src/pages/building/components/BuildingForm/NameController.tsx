import { useEffect } from "react";
import { Form } from "react-bootstrap";
import { Control, Controller, UseFormRegister } from "react-hook-form";
import { SaveBuilding } from "src/hooks/api/building";

type Props = { isPending: boolean; control: Control<SaveBuilding>; register: UseFormRegister<SaveBuilding> };

function NameController({ isPending, control, register }: Props) {
  useEffect(() => {
    register("name", {
      required: "건물 이름을 입력해 주세요.",
      maxLength: { value: 255, message: "255자를 초과 했습니다." },
    });
  }, [register]);

  return (
    <Controller
      name="name"
      control={control}
      render={({ field: { value, ...otherProps }, fieldState: { error }, formState: { isSubmitted } }) => (
        <Form.Group className="mt-3" controlId="name">
          <Form.Label>건물 이름</Form.Label>
          <Form.Control
            disabled={isPending}
            placeholder="Enter building name"
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
