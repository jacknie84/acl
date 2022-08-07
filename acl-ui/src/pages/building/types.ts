import { BuildingFacilitySummary } from "./facility/types";

export type BuildingSummary = { id: number | string; name: string; lastModifiedDate: string };
export type SaveBuilding = Partial<Omit<BuildingSummary, "lastModifiedDate">>;
export type BuildingContextValue = {
  building?: BuildingSummary;
  facilities: Record<string, BuildingFacilitySummary>;
  facilityPath: string[];
};