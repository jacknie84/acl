import { PropsWithChildren, useCallback, useEffect, useMemo, useRef, useState } from "react";
import AuthContext, { defaultAuthValue } from "../contexts/AuthContext";
import { AuthPrincipal, AuthStatus } from "../types";

type Props = { onAuthenticated: () => void };

function AuthProvider({ onAuthenticated, children }: PropsWithChildren<Props>) {
  const initialized = useRef(false);
  const [status, setStatus] = useState<AuthStatus>(defaultAuthValue.status);
  const [principal, setPrincipal] = useState<AuthPrincipal>(defaultAuthValue.principal);
  const authenticate = useCallback(
    (principal: AuthPrincipal) => {
      setPrincipal(principal);
      setStatus("authenticated");
      onAuthenticated();
    },
    [onAuthenticated],
  );
  const revoke = useCallback(() => {
    setPrincipal({});
    setStatus("unauthenticated");
  }, []);
  const providerValue = useMemo(() => ({ status, principal, authenticate, revoke }), [status, principal, authenticate, revoke]);

  useEffect(() => {
    if (initialized.current) {
      return;
    }
    setStatus("unauthenticated");
  }, []);

  useEffect(() => {
    console.log("status", status, "principal", principal);
  }, [status, principal]);

  return <AuthContext.Provider value={providerValue}>{children}</AuthContext.Provider>;
}

export default AuthProvider;
