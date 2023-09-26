import * as ENDPOINTS from "api/Endpoints"
import axios from 'axios';

export const config = {
    headers: {
        'Content-Type': 'application/json'
    }
};

export const getRequest = async (url) => {
    return await axios.get(ENDPOINTS.API_BASE_URL + url, config);
}

export const postRequest = async (url, data) => {
    const token = localStorage.getItem('token');
    // TODO: Put forgotten-password url here!!!
    if (token && url !== ENDPOINTS.API_LOGIN_URL && url !== ENDPOINTS.API_REGISTER_URL)
        config.headers['Authorization'] = `Bearer ${token}`;
    // axios.headers.common["Authorization"] = `Bearer ${decodedToken}`;
    return await axios.post(ENDPOINTS.API_BASE_URL + url, data, config);
}
