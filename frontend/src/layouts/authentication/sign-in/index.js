/**
 =========================================================
 * Material Dashboard 2 React - v2.2.0
 =========================================================

 * Product Page: https://www.creative-tim.com/product/material-dashboard-react
 * Copyright 2023 Creative Tim (https://www.creative-tim.com)

 Coded by www.creative-tim.com

 =========================================================

 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

import React, {useState} from "react";

// react-router-dom components
import {Link, Navigate, useNavigate} from "react-router-dom";

// @mui material components
import Card from "@mui/material/Card";
import Switch from "@mui/material/Switch";
import Grid from "@mui/material/Grid";
import MuiLink from "@mui/material/Link";

// @mui icons
import FacebookIcon from "@mui/icons-material/Facebook";
import GitHubIcon from "@mui/icons-material/GitHub";
import GoogleIcon from "@mui/icons-material/Google";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDInput from "components/MDInput";
import MDButton from "components/MDButton";

// Authentication layout components
import BasicLayout from "layouts/authentication/components/BasicLayout";

// Images
import bgImage from "assets/images/bg-sign-in-basic.jpeg";

import {login} from "api/Authentication";


function Basic() {
    const [rememberMe, setRememberMe] = useState(false);
    const [user, setUser] = useState({});
    const [isLoginLoading, setIsLoginLoading] = useState(false);
    const navigate = useNavigate();


    const handleSetRememberMe = () => setRememberMe(!rememberMe);
    const handleEmailChange = (event) => {
        user.username = event.target.value;
    };
    const handlePasswordChange = (event) => {
        user.password = event.target.value;
    };
    const handleLoginButtonClicked = async () => {
        setIsLoginLoading(true);
        let loginSuccessful = await login(user);
        if(loginSuccessful) {
            navigate('/dashboard');
        }
    };

    return (
        <BasicLayout image={bgImage}>
            <Card>
                <MDBox
                    variant="gradient"
                    bgColor="info"
                    borderRadius="lg"
                    coloredShadow="info"
                    mx={2}
                    mt={-3}
                    p={2}
                    mb={1}
                    textAlign="center"
                >
                    <MDTypography variant="h4" fontWeight="medium" color="white" mt={1}>
                        Login
                    </MDTypography>
                    <Grid container spacing={3} justifyContent="center" sx={{mt: 1, mb: 2}}>
                        <Grid item xs={2}>
                            <MDTypography component={MuiLink} href="#" variant="body1" color="white">
                                <FacebookIcon color="inherit"/>
                            </MDTypography>
                        </Grid>
                        <Grid item xs={2}>
                            <MDTypography component={MuiLink} href="#" variant="body1" color="white">
                                <GitHubIcon color="inherit"/>
                            </MDTypography>
                        </Grid>
                        <Grid item xs={2}>
                            <MDTypography component={MuiLink} href="#" variant="body1" color="white">
                                <GoogleIcon color="inherit"/>
                            </MDTypography>
                        </Grid>
                    </Grid>
                </MDBox>
                <MDBox pt={4} pb={3} px={3}>
                    <MDBox component="form" role="form">
                        <MDBox mb={2}>
                            <MDInput type="email" label="Email" fullWidth onChange={handleEmailChange}/>
                        </MDBox>
                        <MDBox mb={2}>
                            <MDInput type="password" label="Password" fullWidth onChange={handlePasswordChange}/>
                        </MDBox>
                        {/*<MDBox display="flex" alignItems="center" ml={-1}>*/}
                        {/*    <Switch checked={rememberMe} onChange={handleSetRememberMe}/>*/}
                        {/*    <MDTypography*/}
                        {/*        variant="button"*/}
                        {/*        fontWeight="regular"*/}
                        {/*        color="text"*/}
                        {/*        onClick={handleSetRememberMe}*/}
                        {/*        sx={{cursor: "pointer", userSelect: "none", ml: -1}}*/}
                        {/*    >*/}
                        {/*        &nbsp;&nbsp;Remember me*/}
                        {/*    </MDTypography>*/}
                        {/*</MDBox>*/}
                        <MDBox mt={4} mb={1}>
                            <MDButton variant="gradient" color="info" fullWidth onClick={handleLoginButtonClicked}>
                                login
                            </MDButton>
                        </MDBox>
                        <MDBox mt={3} mb={0} textAlign="center">
                            <MDTypography variant="button" color="text">
                                Don&apos;t have an account?{" "}
                                <MDTypography
                                    component={Link}
                                    to="/authentication/register"
                                    variant="button"
                                    color="info"
                                    fontWeight="medium"
                                    textGradient
                                >
                                    Register
                                </MDTypography>
                            </MDTypography>
                        </MDBox>
                        <MDBox mt={0} mb={1} textAlign="center">
                            <MDTypography variant="button" color="text">
                                Forgot your password?{" "}
                                <MDTypography
                                    component={Link}
                                    to="/authentication/password-reset"
                                    variant="button"
                                    color="info"
                                    fontWeight="medium"
                                    textGradient
                                >
                                    Reset password
                                </MDTypography>
                            </MDTypography>
                        </MDBox>
                    </MDBox>
                </MDBox>
            </Card>
        </BasicLayout>
    );
}

export default Basic;
