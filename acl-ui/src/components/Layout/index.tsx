import { Breadcrumb, Container } from "react-bootstrap";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import withAuthRequired from "../auth/hoc/auth-required";
import BreadcrumbTree from "./BreadcrumbTree";
import Navbar from "./Navbar";
import { DomainTree } from "./types";

const domainTree = {
  pattern: "/*",
  path: "/home",
  name: "홈",
  children: [
    {
      pattern: "/home/*",
      path: "/home",
      name: "대시보드",
    },
    {
      pattern: "/members/*",
      path: "/members",
      name: "회원관리",
      children: [
        {
          pattern: "/members/form",
          path: "/members",
          name: "등록",
        },
        {
          pattern: "/members/:id/form",
          path: "/members",
          name: "수정",
        },
      ],
    },
    {
      pattern: "/buildings/*",
      path: "/buildings",
      name: "건물관리",
      children: [
        {
          pattern: "/buildings/form",
          path: "/buildings",
          name: "등록",
        },
        {
          pattern: "/buildings/:id/tabs/*",
          path: "/buildings",
          name: "상세",
        },
      ],
    },
    {
      pattern: "/me/*",
      path: "/me",
      name: "내정보",
    },
  ],
} as DomainTree;

function Layout() {
  return (
    <>
      <Navbar />
      <Container>
        <Breadcrumb className="mt-3">
          <BreadcrumbTree domainTree={domainTree} />
        </Breadcrumb>
        <Outlet />
      </Container>
    </>
  );
}

function Unauthenticated() {
  const location = useLocation();
  return <Navigate to="/getting-start" replace state={{ from: location }} />;
}

export default withAuthRequired(Layout, { unauthenticated: Unauthenticated });
