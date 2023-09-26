import * as ENDPOINTS from "api/Endpoints";
import {postRequest} from "api/RequestHandler";
import React, {useState} from "react";
import {isExpired, decodeToken} from "react-jwt";
// import {Navigate} from "react-router-dom";
// import {useNavigate} from 'react-router-dom';


const axios = require('axios');


export const login = async (credentials) => {
    postRequest(ENDPOINTS.API_LOGIN_URL, {
        loginInput: credentials.username,
        password: credentials.password
    })
        .then(function (response) {
            // const navigate = useNavigate();
            const decodedToken = decodeToken(response.data.access_token);
            const isTokenExpired = isExpired(response.data.access_token);
            if (!isTokenExpired) {
                localStorage.setItem('token', JSON.stringify(response.data.access_token));
                // axios.headers.common["Authorization"] = `Bearer ${decodedToken}`;
                return true;
                // const nav = () => navigate('/dashboard');
                // return <Navigate replace to="/dashboard"/>
            } else {
                logout();
            }
        })
        .catch(function (error) {
            console.log(error);
        });
    return false;
}

export function logout() {
    localStorage.removeItem('token');
    // delete axios.headers.common["Authorization"];
}

