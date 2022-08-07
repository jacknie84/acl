import { Nav, Offcanvas } from "react-bootstrap";
import useAuth from "../../auth/hooks/auth";
import NavLink from "./NavLink";

type Props = { show: boolean; onHide: () => void };

const links = [
  { to: "/home", label: "대시보드" },
  { to: "/members", label: "회원관리" },
  { to: "/buildings", label: "건물관리" },
  { to: "/me", label: "내 정보" },
];

function MenuOffCanvas({ show, onHide }: Props) {
  const { principal } = useAuth();

  return (
    <Offcanvas show={show} onHide={onHide}>
      <Offcanvas.Header closeButton>
        <Offcanvas.Title>{principal.user?.email}</Offcanvas.Title>
      </Offcanvas.Header>
      <Offcanvas.Body className="bg-dark fg-white">
        <Nav className="flex-column" variant="pills" onSelect={onHide}>
          {links.map(({ to, label }) => (
            <NavLink key={to} to={to}>
              {label}
            </NavLink>
          ))}
        </Nav>
      </Offcanvas.Body>
    </Offcanvas>
  );
}

export default MenuOffCanvas;
