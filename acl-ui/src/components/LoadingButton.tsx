import { PropsWithChildren } from "react";
import { Button, Spinner } from "react-bootstrap";
import { Variant } from "react-bootstrap/esm/types";

type Props = {
  isLoading?: boolean;
  type?: "submit" | "reset" | "button";
  onClick?: () => void;
  spinnerProps?: {
    animation?: "border" | "grow";
    variant?: Variant;
  };
};

function LoadingButton({ isLoading = false, type = "button", onClick, spinnerProps = {}, children }: PropsWithChildren<Props>) {
  const { animation = "border", variant } = spinnerProps;

  return (
    <Button disabled={isLoading} type={type} onClick={onClick}>
      {children} {isLoading && <Spinner size="sm" animation={animation} variant={variant} />}
    </Button>
  );
}

export default LoadingButton;
