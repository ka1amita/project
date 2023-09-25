import "api/endpoints"
import React from "react";
import { useJwt } from "react-jwt";



// TODO: Maybe use Axiom for requests?
export const login = async (credentials) => {
    try {
        const response = await fetch(`${API_BASE_URL + API_LOGIN_URL}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(credentials),
        });
        const data = await response.json();
        const { decodedToken, isExpired } = useJwt(data.token);
        // TODO: Set condition for login...

        // TODO: Which one to use: Local storage (persists)
        //  OR Session storage (gets destroyed when user closes browser)
        //   OR State management (needs Redux or Context API)
        // TODO: THIS NEEDS FURTHER RESEARCH...
        // localStorage.setItem('token', yourToken);
        // const token = localStorage.getItem('token');
        // sessionStorage.setItem('token', yourToken);
        // const token = sessionStorage.getItem('token');
        // dispatch({ type: 'SET_TOKEN', payload: yourToken });
        // const token = useSelector(state => state.token);
        return data;
    } catch (error) {
        console.error('Login failed:', error);
        throw error;
    }
};

export const logout = async () => {
    setIsLoggedIn(false);
};

