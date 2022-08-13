import { useCallback, useContext, useMemo } from "react";
import { revokeAsync } from "../client";
import { getTokenAsyncWithRefreshToken, parseTokenResponse } from "../components/token-service";
import tokenStore from "../components/token-store";
import AuthContext from "../contexts/AuthContext";
import { OAuth2Error, OAuth2TokenResponseBody } from "../types";

function useAuth() {
  const { status, principal, authenticate, revoke } = useContext(AuthContext);
  const signIn = useCallback(() => authorize(), []);
  const signOut = useCallback(async () => {
    try {
      const { refresh_token } = await tokenStore.loadAsync();
      revokeAsync(refresh_token);
    } catch (error) {
      console.log(error);
    } finally {
      revoke();
    }
  }, [revoke]);
  const refreshTokenAsync = useCallback(
    async (token: OAuth2TokenResponseBody) => {
      const newToken = await getTokenAsyncWithRefreshToken(token?.refresh_token);
      await tokenStore.saveAsync(newToken);
      const { principal } = parseTokenResponse(newToken);
      authenticate(principal);
      return newToken;
    },
    [authenticate],
  );
  const getIdTokenAsync = useCallback<(forceRefreshToken?: boolean) => Promise<string>>(
    async (forceRefreshToken = false) => {
      const token = await tokenStore.loadAsync();
      if (forceRefreshToken) {
        const newToken = await refreshTokenAsync(token);
        return newToken.id_token;
      } else {
        try {
          parseTokenResponse(token);
          return token.id_token;
        } catch (error) {
          const oauth2Error = error as OAuth2Error;
          if (oauth2Error.type === "token_expired") {
            const newToken = await refreshTokenAsync(token);
            return newToken.id_token;
          } else {
            throw error;
          }
        }
      }
    },
    [refreshTokenAsync],
  );

  return useMemo(
    () => ({ status, principal, signIn, signOut, getIdTokenAsync }),
    [status, principal, signIn, signOut, getIdTokenAsync],
  );
}

function authorize() {
  const params = new URLSearchParams({
    response_type: "code",
    client_id: "acl-ui",
    scope: "openid",
    redirect_uri: "http://127.0.0.1:3000/callback",
  });
  window.location.replace(`http://localhost:8080/oauth2/authorize?${params}`);
}

export default useAuth;
