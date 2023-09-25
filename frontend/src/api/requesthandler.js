import "api/endpoints"

const axios = require('axios');
const config = {
    headers: {
        'Content-Type': 'application/json'
    }
};

function getRequest(url, returnFunction) {
    axios.get(API_BASE_URL + url, config)
        .then(function (response) {
            returnFunction(response);
            // console.log(response);
        })
        .catch(function (error) {
            console.log(error);
        });
}

async function postRequest(url, data, returnFunction) {
    axios.post(API_BASE_URL + url, data, config)
        .then(function (response) {
            returnFunction(response);
            // console.log(response);
        })
        .catch(function (error) {
            console.log(error);
        });
}

async function postFormRequest(url, formName, returnFunction) {
    axios.post(API_BASE_URL + url, document.querySelector(formName), config)
        .then(function (response) {
            returnFunction(response);
            // console.log(response);
        })
        .catch(function (error) {
            console.log(error);
        });
}
