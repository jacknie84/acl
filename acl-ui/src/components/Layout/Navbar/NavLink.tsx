import { PropsWithChildren } from "react";
import { Nav } from "react-bootstrap";
import { Link, useMatch } from "react-router-dom";

type Props = {
  to: string;
};

function NavLink({ to, children }: PropsWithChildren<Props>) {
  const match = useMatch(`${to}/*`);

  return (
    <Nav.Link as={Link} to={to} eventKey={to} active={Boolean(match)}>
      {children}
    </Nav.Link>
  );
}

export default NavLink;
