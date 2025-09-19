import getDecodeLocalStorage from "@/libs/getDecodeLocalStorage";
import axios, { AxiosError, AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from "axios";
import UserController from "./UserController";
import setEncodeLocalStorage from "@/libs/setEncodeLocalStorage";

let url = "";

url = import.meta.env.VITE_API_URL || ""


/*
*DESC:
  API통신을 위한 AXIOS기본 설정
>TODO(24-11-28기준):
  인터셉트 및 계정, 토큰 입력을 위한 로직 추가
*/
class ApiService {

  public publicApiAxios: AxiosInstance;

  private setupInterceptors(axiosInstance: AxiosInstance) {
    // 요청 인터셉터
    axiosInstance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const accessToken = getDecodeLocalStorage({ key: "accessToken" }) || { value: "" }

        if (accessToken.value !== "") {
          config.headers.Authorization = `Bearer ${accessToken?.value}`

          if (!(config.data instanceof FormData)) {
            config.headers["Content-Type"] = "application/json; charset=UTF-8";
            config.headers["Accept"] = "application/json";
          }

        }

        return config;
      },
      (error: AxiosError) => {
        // 요청 에러 처리
        console.error("Request Error:", error);

        return Promise.reject(error);
      }
    );

    // 응답 인터셉터
    axiosInstance.interceptors.response.use(
      (response) => response,
      async (error: AxiosError) => {
        // config가 없으면 재시도 불가
        const originalRequest = error.config as (InternalAxiosRequestConfig & { _retry?: boolean }) | undefined;
        if (!originalRequest) {
          return Promise.reject(error);
        }

        // 서버 에러 판별은 문자열 비교보단 status/메시지 조합 권장
        const isTokenExpired =
          error.response?.status === 401 && (error.response?.data === "Token expired" || (error.response?.data as any)?.message === "Token expired");

        // 무한 루프 방지
        if (isTokenExpired && !originalRequest._retry) {
          originalRequest._retry = true;
          try {
            const res = await UserController.postAuthRefresh(); // 새 accessToken 발급 API
            if (res.status === 200 && res.data?.accessToken) {
              const newToken = res.data.accessToken;

              // 1) 전역 기본 헤더 갱신
              axiosInstance.defaults.headers.common.Authorization = `Bearer ${newToken}`;

              // 2) 이번에 실패했던 요청 헤더 갱신 후 재시도
              originalRequest.headers = originalRequest.headers ?? {};
              originalRequest.headers.Authorization = `Bearer ${newToken}`;

              // 3) 로컬스토리지 갱신(선택)
              setEncodeLocalStorage({ key: "accessToken", value: newToken });

              return axiosInstance(originalRequest);
            }
          } catch (e) {
            window.alert("접근 권한이 없습니다. 다시 로그인해주세요.");
            localStorage.clear();
            window.location.href = "/login";
            return Promise.reject(e);
          }
        }

        return Promise.reject(error);
      }
    );
  }

  constructor() {
    this.publicApiAxios = axios.create({
      baseURL: url,
      timeout: 5000,
    });

    this.setupInterceptors(this.publicApiAxios)
  }

}
const axiosConfig = new ApiService();




export default axiosConfig;
