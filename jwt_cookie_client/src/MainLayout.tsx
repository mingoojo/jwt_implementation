import { Outlet, useLocation, useNavigate } from "react-router-dom";
import Header from "./component/Header";
import UserController from "./controller/UserController";
import { useEffect } from "react";
import { AxiosError } from "axios";

export default function MainLayout() {

  const navigate = useNavigate()
  const location = useLocation()

  const userReqest = async () => {
    try {
      const res = await UserController.getUsers()

      console.log(res.data)

    } catch (error) {
      if (error instanceof AxiosError) {

        if (String(error.status).startsWith("4") && location.pathname !== "/login") {
          window.alert("로그인하세요")
          navigate("/login")
        }

      }
    }
  }


  useEffect(() => {
    userReqest()
  }, [location.pathname])

  return (
    <div >
      <Header />
      <Outlet />
    </div>
  )
}