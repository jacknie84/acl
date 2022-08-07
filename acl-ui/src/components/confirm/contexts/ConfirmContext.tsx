import { createContext } from "react";
import { ConfirmContextValue } from "../types";

const stub = () => {
  throw new Error("unimplemented");
};

export const bareOptions = { publish: stub };
const ConfirmContext = createContext<ConfirmContextValue>(bareOptions);

export default ConfirmContext;
