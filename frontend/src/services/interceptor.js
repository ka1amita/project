import HttpService from "./htttp.service";
import { AuthContext } from "context";
import {useContext} from "react";

export const setupAxiosInterceptors = (onUnauthenticated) => {
  const authContext = useContext(AuthContext);
  const onRequestSuccess = async (config) => {
    const token = localStorage.getItem("token");
    config.headers.Authorization = `Bearer ${token}`;
    return config;
  };
  const onRequestFail = (error) => Promise.reject(error);

  HttpService.addRequestInterceptor(onRequestSuccess, onRequestFail);

  const onResponseSuccess = (response) => {
    authContext.setBackendInfo(response.headers['Environment'], response.headers['Host'])
    return response;
  }

  const onResponseFail = (error) => {
    const status = error.status || error.response.status;
    if (status === 403 || status === 401) {
      onUnauthenticated();
    }

    return Promise.reject(error);
  };
  HttpService.addResponseInterceptor(onResponseSuccess, onResponseFail);
};
