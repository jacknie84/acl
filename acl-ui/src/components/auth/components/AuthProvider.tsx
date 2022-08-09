import { PropsWithChildren, useCallback, useEffect, useMemo, useRef, useState } from "react";
import AuthContext, { defaultAuthValue } from "../contexts/AuthContext";
import { AuthPrincipal, AuthStatus, OAuth2Error, OAuth2TokenResponseBody } from "../types";
import {
  getTokenAsyncWithAuthorizationCode,
  getTokenAsyncWithRefreshToken,
  isAuthorizationCallback,
  parseTokenResponse,
} from "./token-service";
import tokenStore from "./token-store";

type Props = { onAuthenticated: () => void; onError?: (error: any) => void };

function AuthProvider({ onAuthenticated, onError = () => {}, children }: PropsWithChildren<Props>) {
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
    tokenStore.clear();
    setPrincipal({});
    setStatus("unauthenticated");
  }, []);
  const handleSuccessAsync = useCallback(
    async (token: OAuth2TokenResponseBody) => {
      const { principal } = parseTokenResponse(token);
      authenticate(principal);
      await tokenStore.saveAsync(token);
    },
    [authenticate],
  );
  const handleError = useCallback(
    (error: OAuth2Error) => {
      revoke();
      onError(error);
    },
    [revoke, onError],
  );
  const refreshTokenAsync = useCallback(
    async (error: OAuth2Error) => {
      const { type, token } = error;
      if (type === "token_expired") {
        const newToken = await getTokenAsyncWithRefreshToken(token?.refresh_token);
        await handleSuccessAsync(newToken);
      } else {
        throw error;
      }
    },
    [handleSuccessAsync],
  );
  const providerValue = useMemo(() => ({ status, principal, authenticate, revoke }), [status, principal, authenticate, revoke]);

  useEffect(() => {
    Promise.resolve().catch();
    if (initialized.current) {
      return;
    }
    initialized.current = true;
    if (isAuthorizationCallback()) {
      getTokenAsyncWithAuthorizationCode(window.location.href)
        .then((token) => handleSuccessAsync(token))
        .catch((error) => handleError(error));
    } else {
      tokenStore
        .loadAsync()
        .then((token) => handleSuccessAsync(token))
        .catch((errorWithToken) => refreshTokenAsync(errorWithToken))
        .catch((error) => handleError(error));
    }
    setStatus("unauthenticated");
  }, [authenticate, handleSuccessAsync, handleError, refreshTokenAsync]);

  useEffect(() => {
    console.log("status", status, "principal", principal);
  }, [status, principal]);

  return <AuthContext.Provider value={providerValue}>{children}</AuthContext.Provider>;
}

export default AuthProvider;
