export type AuthStatus = "none" | "authenticated" | "unauthenticated";
export type AuthPrincipal = { user?: { email: string } };
export type AuthState = {
  status: AuthStatus;
  principal: AuthPrincipal;
  authenticate: (principal: AuthPrincipal) => void;
  revoke: () => void;
};

export type OAuth2TokenResponseBody = {
  token_type: "Bearer";
  access_token: string;
  expires_in: number;
  scope: string;
  id_token: string;
  refresh_token: string;
};

export type OAuth2ErrorResponseBody = {
  error: OAuth2ErrorCode;
  error_description?: string | null;
  error_uri?: string | null;
};

export type OAuth2ErrorCode =
  | "invalid_request"
  | "invalid_client"
  | "invalid_grant"
  | "invalid_scope"
  | "unauthorized_client"
  | "unsupported_grant_type"
  | "access_denied"
  | "unsupported_response_type"
  | "insufficient_scope"
  | "invalid_token"
  | "server_error"
  | "temporarily_unavailable"
  | "unsupported_token_type"
  | "invalid_redirect_uri";

export class OAuth2Error extends Error {
  type: OAuth2ErrorType;
  statusCode?: number;
  token?: OAuth2TokenResponseBody;
  error?: OAuth2ErrorResponseBody;

  constructor(message?: string, options?: OAuth2ErrorOptions) {
    super(message ?? options?.error?.error_description ?? options?.error?.error);
    this.type = options?.type ?? "none";
    this.statusCode = options?.statusCode;
    this.token = options?.token;
    this.error = options?.error;
  }
}
export type OAuth2ErrorType = "none" | "token_expired" | "exchange";

export type OAuth2ErrorOptions = {
  type: OAuth2ErrorType;
  statusCode?: number;
  token?: OAuth2TokenResponseBody;
  error?: OAuth2ErrorResponseBody;
};

export type GrantType = "authorization_code" | "refresh_token";
