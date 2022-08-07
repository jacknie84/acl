export type AuthStatus = "none" | "authenticated" | "unauthenticated";
export type AuthPrincipal = { user?: { email: string } };
export type AuthState = {
  status: AuthStatus;
  principal: AuthPrincipal;
  authenticate: (principal: AuthPrincipal) => void;
  revoke: () => void;
};
