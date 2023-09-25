import "api/endpoints"
import {postRequest} from "/api/requesthandler"
import React, {useState} from "react";
import {useJwt} from "react-jwt";

const axios = require('axios');

export const login = async (credentials) => {
    await postRequest(API_LOGIN_URL, {
        username: credentials.username,
        password: credentials.password
    }, (response) => {
        const {decodedToken, isExpired} = useJwt(response.token);
        if (!isExpired) {
            localStorage.setItem('token', JSON.stringify(decodedToken));
            axios.defaults.headers.common["Authorization"] = `Bearer ${decodedToken}`;
        } else {
            logout();
        }
    });
};

export const logout = async () => {
    localStorage.removeItem('token');
    delete axios.defaults.headers.common["Authorization"];
};

