import { createContext } from "react";
import { AuthState } from "../types";

const stub = () => {
  throw new Error("unimplemented stub");
};
const defaultAuthValue = { status: "none", principal: {}, authenticate: stub, revoke: stub } as AuthState;
const AuthContext = createContext(defaultAuthValue);

export default AuthContext;
export { defaultAuthValue };
