import { useEffect } from "react";
import { Form } from "react-bootstrap";
import { Control, Controller, UseFormRegister } from "react-hook-form";
import { PutMe } from "src/hooks/api/me";

type Props = {
  isPending: boolean;
  control: Control<PutMe>;
  register: UseFormRegister<PutMe>;
  label: string;
  name: keyof PutMe;
  placeholder: string;
};

function PasswordController({ isPending, control, register, label, name, placeholder }: Props) {
  useEffect(() => {
    register(name, {
      required: "비밀번호를 입력해 주세요.",
      minLength: { value: 6, message: "최소 6자리는 입력 해야 합니다." },
      maxLength: { value: 15, message: "15자리 까지 입력 가능 합니다." },
    });
  }, [register, name]);

  return (
    <Controller
      name={name}
      control={control}
      render={({ field: { value, ...otherProps }, fieldState: { error }, formState: { isSubmitted } }) => (
        <Form.Group className="mb-3" controlId={name}>
          <Form.Label>{label}</Form.Label>
          <Form.Control
            disabled={isPending}
            type="password"
            placeholder={placeholder}
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
