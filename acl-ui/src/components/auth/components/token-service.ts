import jwtDecode, { JwtPayload } from "jwt-decode";
import moment from "moment";
import { tokenAsync } from "../client";
import { AuthPrincipal, OAuth2Error, OAuth2ErrorCode, OAuth2TokenResponseBody } from "../types";

export function isAuthorizationCallback() {
  const url = new URL(window.location.href, window.location.origin);
  return url.pathname === "/callback" && (url.searchParams.has("error") || url.searchParams.has("code"));
}

export async function getTokenAsyncWithAuthorizationCode(callbackUrl: string) {
  const grantType = "authorization_code";
  const url = new URL(callbackUrl, window.location.origin);
  if (url.searchParams.has("error")) {
    const error = url.searchParams.get("error") as OAuth2ErrorCode;
    const error_description = url.searchParams.get("error_description");
    const error_uri = url.searchParams.get("error_uri");
    throw new OAuth2Error(`토큰 요청 에러 발생(${grantType})`, {
      type: "exchange",
      error: { error, error_description, error_uri },
    });
  }
  if (url.searchParams.has("code")) {
    const code = url.searchParams.get("code")!;
    const response = await tokenAsync(grantType, code);
    const responseBody = await response.json();
    if (response.ok) {
      return responseBody;
    } else {
      throw new OAuth2Error(`토큰 요청 에러 발생(${grantType})`, {
        type: "exchange",
        statusCode: response.status,
        error: responseBody,
      });
    }
  }

  throw new Error("bug");
}

export async function getTokenAsyncWithRefreshToken(refreshToken?: string) {
  if (!refreshToken) {
    throw new OAuth2Error("refresh_token 은 필수 값 입니다.");
  }
  const grantType = "refresh_token";
  const response = await tokenAsync(grantType, refreshToken);
  const responseBody = await response.json();
  if (response.ok) {
    return responseBody as OAuth2TokenResponseBody;
  } else {
    throw new OAuth2Error(`토큰 요청 에러 발생(${grantType})`, {
      type: "exchange",
      statusCode: response.status,
      error: responseBody,
    });
  }
}

export function parseTokenResponse(token: OAuth2TokenResponseBody) {
  const { sub, exp } = jwtDecode<JwtPayload>(token.id_token);
  if (exp && moment.unix(exp).isBefore(moment.now())) {
    throw new OAuth2Error("토큰 만료(id_token)", { type: "token_expired", token });
  }
  return { principal: { user: { email: sub! } } as AuthPrincipal };
}
