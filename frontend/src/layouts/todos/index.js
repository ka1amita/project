// @mui material components
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import Footer from "examples/Footer";
import DataTable from "examples/Tables/DataTable";

// Data
import todosTableData from "layouts/todos/data/todosTableData";
import React, {useContext, useEffect, useState} from "react";
import MDInput from "../../components/MDInput";
import AuthService from "../../services/auth-service";
import TodoService from "../../services/todo-service";
import {AuthContext} from "../../context";
import MDAlert from "../../components/MDAlert";
import MDButton from "../../components/MDButton";

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import moment from "moment";

function Todos() {
    const [successSB, setSuccessSB] = useState(false);
    const authContext = useContext(AuthContext);
    const [startDate, setStartDate] = useState(new Date());
    const [columns, setColumns] = useState([]);
    const [rows, setRows] = useState([{
        noData: (
            <MDTypography display="block" variant="button" fontWeight="medium">
                There is no data...
            </MDTypography>
        ),
    }]);

    useEffect(() => {
        refreshTodoTable();
    }, []);


    const refreshTodoTable = () => {
        todosTableData(deleteTodo).then((data) => {
            setColumns(data.columns);
            setRows(data.rows);
        });
    }
    const deleteTodo = (id) => {
        TodoService.delete(id).then(() => refreshTodoTable());
    }

    const openSuccessSB = () => setSuccessSB(true);
    const closeSuccessSB = () => setSuccessSB(false);
    const [successMessage, setSuccessMessage] = useState("");
    const renderSuccessSB = (
        <MDAlert color="success">
            <MDTypography variant="body2" color="white">
                {successMessage}
            </MDTypography>
        </MDAlert>
    );

    const [inputs, setInputs] = useState({
        title: "",
        description: "",
        dueDate: "",
    });

    const [errors, setErrors] = useState({
        titleError: false,
        descriptionError: false,
        dueDateError: false,
        error: false,
        errorText: "",
    });

    const changeHandler = (e) => {
        setInputs({
            ...inputs,
            [e.target.name]: e.target.value,
        });
    };

    const submitHandler = async (e) => {
        e.preventDefault();

        if (inputs.title.trim().length === 0) {
            setErrors(prevErrors => ({
                ...prevErrors,
                titleError: true
            }));
            return;
        } else {
            setErrors(prevErrors => ({
                ...prevErrors,
                titleError: false
            }));
        }

        if (inputs.description.trim().length === 0) {
            setErrors(prevErrors => ({
                ...prevErrors,
                descriptionError: true
            }));
            return;
        } else {
            setErrors(prevErrors => ({
                ...prevErrors,
                descriptionError: false
            }));
        }

        const newTodo = {
            title: inputs.title,
            description: inputs.description,
            dueDate: moment(startDate).format('yyyy-MM-DDTHH:mm:ss'),
            appUser: authContext.getUsername()
        };

        if (newTodo.dueDate.trim().length === 0) {
            setErrors(prevErrors => ({
                ...prevErrors,
                dueDateError: true
            }));
            return;
        } else {
            setErrors(prevErrors => ({
                ...prevErrors,
                dueDateError: false
            }));
        }


        const myData = {
            data: {
                type: "todos",
                attributes: newTodo,
            },
        };

        try {
            const response = await TodoService.add(myData);
            refreshTodoTable();
            setSuccessMessage(response.message);
            openSuccessSB();
            setTimeout(() => {
                closeSuccessSB();
            }, 10000)

            setInputs({
                title: "",
                description: "",
                dueDate: "",
            });

            setErrors({
                titleError: false,
                descriptionError: false,
                dueDateError: false,
                error: false,
                errorText: "",
            });
        } catch (err) {
            if (err.hasOwnProperty("message")) {
                setErrors(prevErrors => ({
                    ...prevErrors,
                    error: true,
                    errorText: err.message
                }));
            } else if (err.hasOwnProperty("error_message")) {
                setErrors(prevErrors => ({
                    ...prevErrors,
                    error: true,
                    errorText: err.error_message
                }));
            } else {
                setErrors(prevErrors => ({
                    ...prevErrors,
                    error: true,
                    errorText: "Unknown error"
                }));
                console.error(err);
            }
        }
    };

    return (
        <DashboardLayout>
            <DashboardNavbar/>
            <MDBox pt={6} pb={3}>
                <Grid container spacing={6}>
                    <Grid item xs={12}>
                        <MDBox
                            component="form"
                            role="form"
                            method="POST"
                            onSubmit={submitHandler}
                            bgColor="inherit"
                            borderRadius="lg"
                            coloredShadow="success"
                            mt={3}
                            p={3}
                            mb={1}
                            textAlign="left"
                        >
                            <MDInput
                                type="text"
                                label="Title"
                                variant="standard"
                                fullWidth
                                name="title"
                                value={inputs.title}
                                onChange={changeHandler}
                                error={errors.titleError}
                                inputProps={{
                                    autoComplete: "title",
                                    form: {
                                        autoComplete: "off",
                                    },
                                }}
                            />
                            {errors.titleError && (
                                <MDTypography variant="caption" color="error" fontWeight="light">
                                    The title can not be empty
                                </MDTypography>
                            )}
                            <MDInput
                                type="text"
                                label="Description"
                                variant="standard"
                                fullWidth
                                name="description"
                                value={inputs.description}
                                onChange={changeHandler}
                                error={errors.descriptionError}
                                inputProps={{
                                    autoComplete: "description",
                                    form: {
                                        autoComplete: "off",
                                    },
                                }}
                            />
                            {errors.descriptionError && (
                                <MDTypography variant="caption" color="error" fontWeight="light">
                                    The description can not be empty
                                </MDTypography>
                            )}
                            <MDBox
                                bgColor="none"
                                mt={3}
                                mb={1}
                                textAlign="left"
                            >
                                <DatePicker
                                    selected={startDate}
                                    onChange={(date) => setStartDate(date)}
                                    showTimeSelect
                                    timeFormat="HH:mm"
                                    timeIntervals={15}
                                    timeCaption="Time"
                                    dateFormat="yyyy.MM.dd   HH:mm"
                                />
                            </MDBox>
                            <MDButton variant="gradient" color="info" type="submit" sx={{mt: 5}}>
                                add todo
                            </MDButton>
                        </MDBox>
                        <MDBox mt={3} mb={1} textAlign="center">
                            {successSB && (renderSuccessSB)}
                        </MDBox>
                    </Grid>
                    <Grid item xs={12}>
                        <Card>
                            <MDBox
                                mx={2}
                                mt={-3}
                                py={3}
                                px={2}
                                variant="gradient"
                                bgColor="info"
                                borderRadius="lg"
                                coloredShadow="info"
                            >
                                <MDTypography variant="h6" color="white">
                                    Todos
                                </MDTypography>
                            </MDBox>
                            <MDBox pt={3}>
                                <DataTable
                                    table={{columns, rows}}
                                    isSorted={false}
                                    entriesPerPage={false}
                                    showTotalEntries={false}
                                    noEndBorder
                                />
                            </MDBox>
                        </Card>
                    </Grid>
                </Grid>
            </MDBox>
            <Footer/>
        </DashboardLayout>
    );
}

export default Todos;
