import HttpService from "./htttp.service";

class TodoService {
    apiEndpointBase = "api/todos";

    list = async () => {
        const endpoint = this.apiEndpointBase;
        return await HttpService.get(endpoint);
    };

    add = async (payload) => {
        const endpoint = this.apiEndpointBase;
        const requestData = payload.data.attributes
        requestData.due_date = requestData.startDate;
        requestData.app_user = requestData.appUser;
        delete requestData.startDate;
        delete requestData.appUser;
        return await HttpService.post(endpoint, requestData);
    };
    delete = async (id) => {
        const endpoint = this.apiEndpointBase + "/" + id;
        return await HttpService.delete(endpoint);
    };
    edit = async (payload) => {
        const endpoint = this.apiEndpointBase + "/" + payload.id;
        const requestData = payload
        delete requestData.id;
        return await HttpService.patch(endpoint, requestData);
    };
}

export default new TodoService();
