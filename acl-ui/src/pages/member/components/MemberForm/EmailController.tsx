import { useEffect } from "react";
import { Form } from "react-bootstrap";
import { Control, Controller, UseFormRegister } from "react-hook-form";
import { MemberAccount } from "src/hooks/api/member-account";
import { isEmail } from "src/utils/validation";

type Props = { isPending: boolean; control: Control<Partial<MemberAccount>>; register: UseFormRegister<Partial<MemberAccount>> };

function EmailController({ isPending, control, register }: Props) {
  useEffect(() => {
    register("email", {
      required: "이메일을 입력해 주세요.",
      maxLength: { value: 255, message: "255자를 초과 했습니다." },
      validate: {
        email: (v) => (isEmail(v ?? "") ? true : "유효한 이메일이 아닙니다."),
      },
    });
  }, [register]);

  return (
    <Controller
      name="email"
      control={control}
      render={({ field: { value, ...otherProps }, fieldState: { error }, formState: { isSubmitted } }) => (
        <Form.Group className="mt-3" controlId="email">
          <Form.Label>이메일</Form.Label>
          <Form.Control
            disabled={isPending}
            placeholder="Enter email"
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

export default EmailController;
