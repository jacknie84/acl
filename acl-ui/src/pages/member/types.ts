export type MemberRole = "ROLE_USER" | "ROLE_ADMIN";
export type Member = { id: string | number; email: string; password: string; roles: MemberRole[]; lastModifiedDate: string };
export type MemberSummary = { id: string | number; email: string; lastModifiedDate: string };
export type SaveMember = Partial<Member>;
