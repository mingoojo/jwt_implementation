import MainLayout from "./MainLayout";
import LoginPage from "./pages/LoginPage";
import MainPage from "./pages/MainPage";
import SignupPage from "./pages/SignupPage";


const mainRoutes = [
  {
    element: <MainLayout />,
    children: [
      { path: "/", element: <MainPage /> },
      { path: "/login", element: <LoginPage /> },
      { path: "/signup", element: <SignupPage /> },
      { path: "/*", element: <div>Not Found</div> },
    ],
  },
]

export default mainRoutes
