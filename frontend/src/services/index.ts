import axios from "axios";
import type { AxiosRequestConfig, AxiosResponse, AxiosInstance } from "axios";
import { ElMessage } from "element-plus";
import { getToken } from "@/util/CommonUtil";
import router from "@/router";

// create an axois instance with a custom config.
const Api = axios.create({
  baseURL: "http://localhost:9111/",
  timeout: 10000,
  headers: {
    "Content-Type": "application/x-www-form-urlencoded",
  },
});

// interceptor before request.
Api.interceptors.request.use(
  (config) => {
    config.headers["Authorization"] = getToken();
    return config;
  },
  (error) => {
    errorHandler(error);
  }
);

// interceptor after response.
Api.interceptors.response.use(
  (response: AxiosResponse) => {
    let resData = response.data;

    if (resData.code === 401) {
      ElMessage.error("登录过期，请重新登录");
      router.push("/");

      // do not respond with data.
      return Promise.reject(new Error("登录过期，请重新登录"));
    }

    return response;
  },
  (error) => {
    errorHandler(error);
  }
);

function errorHandler(error: any) {
  ElMessage.error("系统异常，请联系管理员");
  return Promise.reject(error);
}

export default Api;
