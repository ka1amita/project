import HttpService from "./htttp.service";

class AuthService {
  // authEndpoint = process.env.API_URL;

  login = async (payload) => {
    const loginEndpoint = 'login';
    const requestData= payload.data.attributes;
    requestData.loginInput = requestData.email;
    delete requestData.email;
    return await HttpService.post(loginEndpoint, requestData);
  };

  register = async (credentials) => {
    const registerEndpoint = 'register';
    const requestData = credentials.data.attributes;
    requestData.username = requestData.name;
    delete requestData.name;
    delete requestData.password_confirmation;
    return await HttpService.post(registerEndpoint, requestData);
  };

  logout = async () => {
    const logoutEndpoint = 'logout';
    return await HttpService.post(logoutEndpoint);
  };

  forgotPassword = async (payload) => {
    const forgotPassword = 'reset';
    const requestData = payload.data.attributes;
    // delete requestData.redirect_url;
    return await HttpService.post(forgotPassword, requestData);
  }

  resetPassword = async (credentials) => {
    const requestData = credentials.data.attributes;
    const resetPassword = 'reset/' + requestData.token;
    delete requestData.password_confirmation;
    delete requestData.token;
    return await HttpService.post(resetPassword, requestData);
  }

  getProfile = async() => {
    const getProfile = 'me';
    return await HttpService.get(getProfile);
  }

  updateProfile = async (newInfo) => {
    const updateProfile = "me";
    return await HttpService.patch(updateProfile, newInfo);
  }
}

export default new AuthService();
