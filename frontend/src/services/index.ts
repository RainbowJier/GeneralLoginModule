import axios from 'axios';
import type { AxiosRequestConfig, AxiosResponse,AxiosInstance } from 'axios'; 
import { ElMessage } from "element-plus"; 
import {getToken} from "@/util/commonUtil";

// create an axois instance with a custom config.
const Api = axios.create({
  baseURL: "http://localhost:9000/",
  timeout: 10000,
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  }
});

// interceptor before request.
Api.interceptors.request.use(
  (config) => {
    config.headers["token"] = getToken();
    return config;
  },
  (error) => {
    errorHandler(error);
  }
);

function errorHandler(error:any){
  ElMessage.error("未知错误，请联系管理员");
  return Promise.reject(error);
}

export default Api;
