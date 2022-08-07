import { ThemeProvider } from "react-bootstrap";
import { BrowserRouter } from "react-router-dom";
import Main from "./Main";

function App() {
  return (
    <ThemeProvider>
      <BrowserRouter>
        <Main />
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
