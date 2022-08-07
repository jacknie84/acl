import { useEffect } from "react";
import { Form } from "react-bootstrap";
import { Control, Controller, UseFormRegister } from "react-hook-form";
import { SaveMember } from "../../types";

type Props = { isPending: boolean; control: Control<SaveMember>; register: UseFormRegister<SaveMember> };

function PasswordController({ isPending, control, register }: Props) {
  useEffect(() => {
    register("password", {
      required: "비밀번호를 입력해 주세요.",
      minLength: { value: 6, message: "최소 6자리는 입력 해야 합니다." },
      maxLength: { value: 13, message: "13자리 까지 입력 가능 합니다." },
    });
  }, [register]);

  return (
    <Controller
      name="password"
      control={control}
      render={({ field: { value, ...otherProps }, fieldState: { error }, formState: { isSubmitted } }) => (
        <Form.Group className="mb-3" controlId="password">
          <Form.Label>비밀번호</Form.Label>
          <Form.Control
            disabled={isPending}
            type="password"
            placeholder="Password"
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

export default PasswordController;
