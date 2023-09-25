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
    return await axios.post(ENDPOINTS.API_BASE_URL + url, data, config);
}
