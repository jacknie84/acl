import { Button, Container, Stack } from "react-bootstrap";
import useAuth from "../components/auth/hooks/auth";

function GettingStart() {
  const { signIn } = useAuth();

  return (
    <Container>
      <Stack>
        <h1 className="display-1">Access Control List</h1>
        <Button size="lg" onClick={() => signIn()}>
          시작하기
        </Button>
      </Stack>
    </Container>
  );
}

export default GettingStart;
