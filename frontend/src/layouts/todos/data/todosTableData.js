// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDBadge from "components/MDBadge";

import TodoService from "services/todo-service";
import {useEffect, useState} from "react";
import Link from "@mui/material/Link";
import moment from "moment";

export default async function data(deleteDataFunction) {
    const todoData = TodoService.list();


    const transformRawData = (allRawData) =>
        allRawData.map(rawData => {
            if (rawData.hasOwnProperty("id") && rawData.hasOwnProperty("title") && rawData.hasOwnProperty("description") &&
                rawData.hasOwnProperty("due_date") && rawData.hasOwnProperty("app_user")) return {
                title: (
                    <MDTypography display="block" variant="button" fontWeight="medium">
                        {rawData.title}
                    </MDTypography>
                ),
                description: (
                    <MDTypography display="block" variant="button" fontWeight="medium">
                        {rawData.description}
                    </MDTypography>
                ),
                dueDate: (
                    <MDTypography display="block" variant="button" fontWeight="medium">
                        {moment(rawData.due_date).format("yyyy.MM.DD  HH:mm")}
                    </MDTypography>
                ),
                appUser: (
                    <MDTypography display="block" variant="button" fontWeight="medium">
                        {rawData.app_user}
                    </MDTypography>
                ),
                actions: (
                    <MDBox>
                        {/*<MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium" mr={2}>*/}
                        {/*    Edit*/}
                        {/*</MDTypography>*/}
                        <MDTypography onClick={() => deleteDataFunction(rawData.id)} component="a" href="#"
                                      variant="caption" color="text" fontWeight="medium">
                            Delete
                        </MDTypography>
                    </MDBox>
                ),
            }
        });

    return {
        columns: [
            {Header: "title", accessor: "title", align: "left"},
            {Header: "description", accessor: "description", width: "45%", align: "left"},
            {Header: "due Date", accessor: "dueDate", align: "center"},
            {Header: "user", accessor: "appUser", align: "center"},
            {Header: "", accessor: "actions", align: "right"},
        ],
        rows: transformRawData(await todoData)
    };

}
