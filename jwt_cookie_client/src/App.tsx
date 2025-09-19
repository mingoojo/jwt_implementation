import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { RecoilRoot } from "recoil";
import { ThemeProvider } from "styled-components";

import { Reset } from "styled-reset";
import GlobalStyle from "./styles/Globalstyle";
import mainRoutes from "./mainRoutes";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import defaultTheme from "./styles/defaultTheme";



const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000, // 기본 10초
      retry: 2, // 실패 시 1회 재시도
      refetchOnWindowFocus: false, // 포커스 시 자동 리패치 비활성화(선호에 따라)
    },
  },
});


function App() {

  const router = createBrowserRouter([...mainRoutes])

  return (
    <QueryClientProvider client={queryClient}>
      <RecoilRoot>
        <ThemeProvider theme={defaultTheme}>
          <ReactQueryDevtools initialIsOpen={false} position="bottom" />
          <Reset />
          <GlobalStyle />
          <RouterProvider router={router} />
        </ThemeProvider>
      </RecoilRoot>
    </QueryClientProvider>

  );
}

export default App;
