import { useState } from "react";
import { Container, Navbar as BsNavbar } from "react-bootstrap";
import { GiHamburgerMenu } from "react-icons/gi";
import { useNavigate } from "react-router-dom";
import MenuOffCanvas from "./MenuOffCanvas";

function Navbar() {
  const navigate = useNavigate();
  const [showOffCanvas, setShowOffCanvas] = useState(false);

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
        </Container>
      </BsNavbar>
      <MenuOffCanvas show={showOffCanvas} onHide={() => setShowOffCanvas(false)} />
    </>
  );
}

export default Navbar;
