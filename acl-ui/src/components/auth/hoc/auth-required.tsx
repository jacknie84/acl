import { useMemo } from "react";
import useAuth from "../hooks/auth";
import { AuthPrincipal } from "../types";

type Options = {
  unauthenticated?: () => JSX.Element;
  isValid?: (principal: AuthPrincipal) => boolean;
};

const defaultOptions = {
  unauthenticated: () => <></>,
  isValid: () => true,
};

function withAuthRequired<P>(element: (props: P) => JSX.Element, options: Options = defaultOptions) {
  const Component = element;
  const Unauthenticated = options.unauthenticated ?? defaultOptions.unauthenticated;
  const isValid = options.isValid ?? defaultOptions.isValid;

  return (props: P) => {
    const { status, principal } = useAuth();
    const isAuthenticated = useMemo(() => status === "authenticated" && isValid(principal), [status, principal]);

    if (!isAuthenticated) {
      return <Unauthenticated />;
    }

    return <Component {...props} />;
  };
}

export default withAuthRequired;
