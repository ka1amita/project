import {useContext, useEffect, useState} from "react";

// react-router-dom components
import {Link, useParams} from "react-router-dom";

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
import BasicLayoutLanding from "layouts/authentication/components/BasicLayoutLanding";

// Images
import bgImage from "assets/images/bg-sign-in-basic.jpeg";

import AuthService from "services/auth-service";
import { AuthContext } from "context";
import MDAlert from "../../components/MDAlert";

function Login() {
  const authContext = useContext(AuthContext);

  let {token} = useParams();
  const [user, setUser] = useState({});
  const [credentialsErros, setCredentialsError] = useState(null);
  const [rememberMe, setRememberMe] = useState(false);

  const [successSB, setSuccessSB] = useState(false);
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
    email: "",
    password: "",
  });

  const [errors, setErrors] = useState({
    emailError: false,
    passwordError: false,
  });

  useEffect(() => {
    if(token) {
      AuthService.activateUser(token).then(r => {
        setSuccessMessage(r.message);
        openSuccessSB();
        setTimeout(() => {
          closeSuccessSB();
        }, 10000)
      }).catch(err => {
        console.log(err);
      });
    }
  }, []);

  const addUserHandler = (newUser) => setUser(newUser);

  const handleSetRememberMe = () => setRememberMe(!rememberMe);

  const changeHandler = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value,
    });
  };

  const submitHandler = async (e) => {
    e.preventDefault();

    closeSuccessSB();

    const passwordFormat = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!"#$%&'()*+,-./:;<=>?@\[\]\\^_`{|}~]).{8,}$/;

    if (inputs.email.trim().length === 0) {
      setErrors(prevErrors => ({
        ...prevErrors,
        emailError: true
      }));
      return;
    } else {
      setErrors(prevErrors => ({
        ...prevErrors,
        emailError: false
      }));
    }

    if (inputs.password.trim().length < 8 || !inputs.password.trim().match(passwordFormat)) {
      setErrors(prevErrors => ({
        ...prevErrors,
        passwordError: true
      }));
      return;
    } else {
      setErrors(prevErrors => ({
        ...prevErrors,
        passwordError: false
      }));
    }

    const newUser = { email: inputs.email, password: inputs.password };
    addUserHandler(newUser);

    const myData = {
      data: {
        type: "token",
        attributes: { ...newUser },
      },
    };

    try {
      const response = await AuthService.login(myData);
      authContext.login(response.access_token, response.refresh_token);
    } catch (res) {
      if (res.hasOwnProperty("message")) {
        setCredentialsError(res.message);
      } else if (res.hasOwnProperty("error_message")) {
        setCredentialsError(res.error_message);
      } else {
        setCredentialsError("Unknown error");
        console.log(res);
      }
    }

    return () => {
      setInputs({
        email: "",
        password: "",
      });

      setErrors({
        emailError: false,
        passwordError: false,
      });
    };
  };

  return (
    <BasicLayoutLanding image={bgImage}>
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
            Sign in
          </MDTypography>
          <Grid container spacing={3} justifyContent="center" sx={{ mt: 1, mb: 2 }}>
            <Grid item xs={2}>
              <MDTypography component={MuiLink} href="#" variant="body1" color="white">
                <FacebookIcon color="inherit" />
              </MDTypography>
            </Grid>
            <Grid item xs={2}>
              <MDTypography component={MuiLink} href="#" variant="body1" color="white">
                <GitHubIcon color="inherit" />
              </MDTypography>
            </Grid>
            <Grid item xs={2}>
              <MDTypography component={MuiLink} href="#" variant="body1" color="white">
                <GoogleIcon color="inherit" />
              </MDTypography>
            </Grid>
          </Grid>
        </MDBox>
        <MDBox pt={4} pb={3} px={3}>
          <MDBox component="form" role="form" method="POST" onSubmit={submitHandler}>
            <MDBox mb={2}>
              <MDInput
                type="text"
                label="Email or Username"
                fullWidth
                value={inputs.email}
                name="email"
                onChange={changeHandler}
                error={errors.emailError}
              />
            </MDBox>
            <MDBox mb={2}>
              <MDInput
                type="password"
                label="Password"
                fullWidth
                name="password"
                value={inputs.password}
                onChange={changeHandler}
                error={errors.passwordError}
              />
            </MDBox>
            {/*<MDBox display="flex" alignItems="center" ml={-1}>*/}
            {/*  <Switch checked={rememberMe} onChange={handleSetRememberMe} />*/}
            {/*  <MDTypography*/}
            {/*    variant="button"*/}
            {/*    fontWeight="regular"*/}
            {/*    color="text"*/}
            {/*    onClick={handleSetRememberMe}*/}
            {/*    sx={{ cursor: "pointer", userSelect: "none", ml: -1 }}*/}
            {/*  >*/}
            {/*    &nbsp;&nbsp;Remember me*/}
            {/*  </MDTypography>*/}
            {/*</MDBox>*/}
            <MDBox mt={4} mb={1}>
              <MDButton variant="gradient" color="info" fullWidth type="submit">
                sign in
              </MDButton>
            </MDBox>
            {credentialsErros && (
              <MDTypography variant="caption" color="error" fontWeight="light">
                {credentialsErros}
              </MDTypography>
            )}
            <MDBox mt={3} mb={1} textAlign="center">
              <MDTypography variant="button" color="text">
                Forgot your password? Reset it{" "}
                <MDTypography
                  component={Link}
                  to="/auth/forgot-password"
                  variant="button"
                  color="info"
                  fontWeight="medium"
                  textGradient
                >
                  here
                </MDTypography>
              </MDTypography>
            </MDBox>
            <MDBox mb={1} textAlign="center">
              <MDTypography variant="button" color="text">
                Don&apos;t have an account?{" "}
                <MDTypography
                  component={Link}
                  to="/auth/register"
                  variant="button"
                  color="info"
                  fontWeight="medium"
                  textGradient
                >
                  Sign up
                </MDTypography>
              </MDTypography>
            </MDBox>
          </MDBox>
        </MDBox>
      </Card>
      <MDBox mt={3} mb={1} textAlign="center">
        {successSB && (renderSuccessSB)}
      </MDBox>
    </BasicLayoutLanding>
  );
}

export default Login;
