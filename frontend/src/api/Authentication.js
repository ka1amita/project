import * as ENDPOINTS from "api/Endpoints";
import {postRequest} from "api/RequestHandler";
import React, {useState} from "react";
import {useJwt} from "react-jwt";
// import {Navigate} from "react-router-dom";
// import {useNavigate} from 'react-router-dom';


const axios = require('axios');

// const navigate = useNavigate();


export function login(credentials) {
    postRequest(ENDPOINTS.API_LOGIN_URL, {
        loginInput: credentials.username,
        password: credentials.password
    })
        .then(function (response) {
            const {decodedToken, isExpired} = useJwt(response.token);
            if (!isExpired) {
                localStorage.setItem('token', JSON.stringify(decodedToken));
                axios.defaults.headers.common["Authorization"] = `Bearer ${decodedToken}`;
                // const nav = () => navigate('/dashboard');
                // return <Navigate replace to="/dashboard"/>
            } else {
                logout();
            }
        })
        .catch(function (error) {
            console.log(error);
        });
}

export function logout() {
    localStorage.removeItem('token');
    delete axios.defaults.headers.common["Authorization"];
}

