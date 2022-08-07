import { useCallback, useContext, useMemo } from "react";
import AuthContext from "../contexts/AuthContext";

function useAuth() {
  const { status, principal, authenticate, revoke } = useContext(AuthContext);
  const signIn = useCallback(() => authenticate({ user: { email: "jacknie8407@gmail.com" } }), [authenticate]);
  const signOut = useCallback(() => revoke(), [revoke]);
  const value = useMemo(() => ({ status, principal, signIn, signOut }), [status, principal, signIn, signOut]);

  return value;
}

export default useAuth;
