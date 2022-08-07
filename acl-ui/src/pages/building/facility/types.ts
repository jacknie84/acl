export type BuildingFacilitySummary = { id: number | string; name: string; lastModifiedDate: string };
export type SaveBuildingFacility = {
  id?: number | string;
  name?: string;
  acl?: { ownerMemberId?: number | string; inheriting?: boolean; entries?: AclEntry[] };
};
export type AclEntry = {
  sid: string;
  label: string;
  read?: boolean;
  write?: boolean;
  create?: boolean;
  remove?: boolean;
  admin?: boolean;
};
