import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ThemeProvider } from "react-bootstrap";
import { BrowserRouter } from "react-router-dom";
import Main from "./Main";

const client = new QueryClient();

function App() {
  return (
    <ThemeProvider>
      <BrowserRouter>
        <QueryClientProvider client={client}>
          <Main />
        </QueryClientProvider>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
