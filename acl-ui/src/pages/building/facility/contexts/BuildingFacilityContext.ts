import { createContext } from "react";
import { BuildingFacility } from "src/hooks/api/building-facility";

export type BuildingFacilityContextValue = { facilityPath: BuildingFacility[] };

export const defaultValue = { facilityPath: [] } as BuildingFacilityContextValue;

export default createContext(defaultValue);
