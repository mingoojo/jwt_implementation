import { AxiosResponse } from "axios";
import axiosConfig from "./axiosConfig";
import { LoginResponse } from "./interface";

const getUsers = async () => {
  const res = await axiosConfig.publicApiAxios({
    url: "/auth/me",
    method: "get",
    withCredentials: true,
  });

  return res
}

const postSignup = async ({
  username,
  email,
  password,
  department,
  position,
  role }: { username: string, email: string, password: string, department: string, position: string, role: string }) => {
  const res = await axiosConfig.publicApiAxios({
    url: "/auth/signup",
    method: "post",
    withCredentials: true,
    data: {
      username,
      email,
      password,
      department,
      position,
      role,
    },
  });

  return res
}

const postLogin = async ({ email, password }: { email: string, password: string }): Promise<AxiosResponse<LoginResponse>> => {
  const res = await axiosConfig.publicApiAxios({
    url: "/auth/login",
    method: "post",
    withCredentials: true,
    data: {
      email,
      password,
    },
  });
  return res
}

const postAuthRefresh = async (): Promise<AxiosResponse<LoginResponse>> => {
  const res = await axiosConfig.publicApiAxios({
    url: "/auth/refresh",
    method: "post",
    withCredentials: true,
  });
  return res
}


const UserController = {
  getUsers,
  postSignup,
  postLogin,
  postAuthRefresh,
}

export default UserController;