import { GrantType, OAuth2Error } from "./types";

export async function tokenAsync(grantType: GrantType, token: string) {
  const headers = { "Content-Type": "application/x-www-form-urlencoded" };
  let params;
  switch (grantType) {
    case "authorization_code":
      params = {
        client_id: "acl-ui",
        client_secret: "secret",
        redirect_uri: "http://127.0.0.1:3000/callback",
        grant_type: grantType,
        code: token,
      };
      break;
    case "refresh_token":
      params = {
        client_id: "acl-ui",
        client_secret: "secret",
        grant_type: grantType,
        refresh_token: token,
      };
      break;
    default:
      throw new OAuth2Error(`지원 하지 않는 grant_type(${grantType}) 입니다.`);
  }
  const body = new URLSearchParams(params);
  return await fetch("http://localhost:8080/oauth2/token", { method: "post", headers, body });
}

export async function revokeAsync(token: string) {
  const headers = { "Content-Type": "application/x-www-form-urlencoded" };
  const body = new URLSearchParams({ client_id: "acl-ui", client_secret: "secret", token });
  return await fetch("http://localhost:8080/oauth2/revoke", { method: "post", headers, body });
}
