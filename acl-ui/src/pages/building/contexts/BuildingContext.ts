import { createContext } from "react";
import { BuildingContextValue } from "../types";

export const defaultValue = { facilities: {}, facilityPath: [] } as BuildingContextValue;

export default createContext<BuildingContextValue>(defaultValue);
