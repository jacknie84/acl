import { useCallback, useState } from "react";
import { Container, Navbar as BsNavbar } from "react-bootstrap";
import { GiHamburgerMenu } from "react-icons/gi";
import { useNavigate } from "react-router-dom";
import useAuth from "src/components/auth/hooks/auth";
import appStateStore from "src/utils/app-state-store";
import MenuOffCanvas from "./MenuOffCanvas";

function Navbar() {
  const navigate = useNavigate();
  const { signOut } = useAuth();
  const [showOffCanvas, setShowOffCanvas] = useState(false);
  const onClickLogout = useCallback(async () => {
    appStateStore.clear();
    await signOut();
    navigate("/", { replace: true });
  }, [signOut, navigate]);

  return (
    <>
      <BsNavbar bg="dark">
        <Container>
          <BsNavbar.Brand>
            <GiHamburgerMenu onClick={() => setShowOffCanvas(true)} size={30} className="fg-blue me-1" />
            <span className="text-white" onClick={() => navigate("/home")}>
              Buildings owner
            </span>
          </BsNavbar.Brand>
          <BsNavbar.Toggle />
          <BsNavbar.Collapse className="justify-content-end">
            <BsNavbar.Text className="text-white" role="button" onClick={onClickLogout}>
              로그아웃
            </BsNavbar.Text>
          </BsNavbar.Collapse>
        </Container>
      </BsNavbar>
      <MenuOffCanvas show={showOffCanvas} onHide={() => setShowOffCanvas(false)} />
    </>
  );
}

export default Navbar;
