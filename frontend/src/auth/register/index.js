import { useContext, useState } from "react";
// react-router-dom components
import { Link } from "react-router-dom";

// @mui material components
import Card from "@mui/material/Card";
import Checkbox from "@mui/material/Checkbox";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDInput from "components/MDInput";
import MDButton from "components/MDButton";
import MDAlert from "components/MDAlert";

// Authentication layout components
import CoverLayout from "layouts/authentication/components/CoverLayout";

// Images
import bgImage from "assets/images/bg-sign-up-cover.jpeg";

import AuthService from "services/auth-service";
import { AuthContext } from "context";
import { InputLabel } from "@mui/material";

function Register() {
  const [successSB, setSuccessSB] = useState(false);
  const authContext = useContext(AuthContext);
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
    name: "",
    email: "",
    password: "",
    agree: false,
  });

  const [errors, setErrors] = useState({
    nameError: false,
    emailError: false,
    passwordError: false,
    agreeError: false,
    error: false,
    errorText: "",
  });
  // const [error, setError]

  const changeHandler = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value,
    });
  };

  const submitHandler = async (e) => {
    e.preventDefault();

    const mailFormat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    const passwordFormat = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!"#$%&'()*+,-./:;<=>?@\[\]\\^_`{|}~]).{8,}$/;

    if (inputs.name.trim().length === 0) {
      setErrors(prevErrors => ({
        ...prevErrors,
        nameError: true
      }));
      return;
    } else {
      setErrors(prevErrors => ({
        ...prevErrors,
        nameError: false
      }));
    }

    if (inputs.email.trim().length === 0 || !inputs.email.trim().match(mailFormat)) {
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

    // if (inputs.agree === false) {
    //   setErrors({ ...errors, agreeError: true });
    //   return;
    // }

    const newUser = { name: inputs.name, email: inputs.email, password: inputs.password };

    const myData = {
      data: {
        type: "users",
        attributes: { ...newUser, password_confirmation: newUser.password },
        relationships: {
          roles: {
            data: [
              {
                type: "roles",
                id: "1",
              },
            ],
          },
        },
      },
    };

    try {
      const response = await AuthService.register(myData);
      setSuccessMessage(response.message);
      openSuccessSB();
      setTimeout(() => {
        closeSuccessSB();
        // authContext.register();
      }, 10000)

      setInputs({
        name: "",
        email: "",
        password: "",
        agree: false,
      });

      setErrors({
        nameError: false,
        emailError: false,
        passwordError: false,
        agreeError: false,
        error: false,
        errorText: "",
      });
    } catch (err) {
      if(err.hasOwnProperty("message")) {
        setErrors(prevErrors => ({
          ...prevErrors,
          error: true,
          errorText: err.message
        }));
      } else if(err.hasOwnProperty("error_message")) {
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
    <CoverLayout image={bgImage}>
      <Card>
        <MDBox
          variant="gradient"
          bgColor="info"
          borderRadius="lg"
          coloredShadow="success"
          mx={2}
          mt={-3}
          p={3}
          mb={1}
          textAlign="center"
        >
          <MDTypography variant="h4" fontWeight="medium" color="white" mt={1}>
            Join us today
          </MDTypography>
          <MDTypography display="block" variant="button" color="white" my={1}>
            Enter your email and password to register
          </MDTypography>
        </MDBox>
        <MDBox pt={4} pb={3} px={3}>
          <MDBox component="form" role="form" method="POST" onSubmit={submitHandler}>
            <MDBox mb={2}>
              <MDInput
                type="text"
                label="Name"
                variant="standard"
                fullWidth
                name="name"
                value={inputs.name}
                onChange={changeHandler}
                error={errors.nameError}
                inputProps={{
                  autoComplete: "name",
                  form: {
                    autoComplete: "off",
                  },
                }}
              />
              {errors.nameError && (
                <MDTypography variant="caption" color="error" fontWeight="light">
                  The name can not be empty
                </MDTypography>
              )}
            </MDBox>
            <MDBox mb={2}>
              <MDInput
                type="email"
                label="Email"
                variant="standard"
                fullWidth
                value={inputs.email}
                name="email"
                onChange={changeHandler}
                error={errors.emailError}
                inputProps={{
                  autoComplete: "email",
                  form: {
                    autoComplete: "off",
                  },
                }}
              />
              {errors.emailError && (
                <MDTypography variant="caption" color="error" fontWeight="light">
                  The email must be valid
                </MDTypography>
              )}
            </MDBox>
            <MDBox mb={2}>
              <MDInput
                type="password"
                label="Password"
                variant="standard"
                fullWidth
                name="password"
                value={inputs.password}
                onChange={changeHandler}
                error={errors.passwordError}
              />
              {errors.passwordError && (
                <MDTypography variant="caption" color="error" fontWeight="light">
                  Password must contain at least 8 characters, including at least 1 lower case, 1 upper case, 1 number, and 1 special character.
                </MDTypography>
              )}
            </MDBox>
            {/*<MDBox display="flex" alignItems="center" ml={-1}>*/}
            {/*  <Checkbox name="agree" id="agree" onChange={changeHandler} />*/}
            {/*  <InputLabel*/}
            {/*    variant="standard"*/}
            {/*    fontWeight="regular"*/}
            {/*    color="text"*/}
            {/*    sx={{ lineHeight: "1.5", cursor: "pointer" }}*/}
            {/*    htmlFor="agree"*/}
            {/*  >*/}
            {/*    &nbsp;&nbsp;I agree to the&nbsp;*/}
            {/*  </InputLabel>*/}
            {/*  <MDTypography*/}
            {/*    component={Link}*/}
            {/*    to="/auth/login"*/}
            {/*    variant="button"*/}
            {/*    fontWeight="bold"*/}
            {/*    color="info"*/}
            {/*    textGradient*/}
            {/*  >*/}
            {/*    Terms and Conditions*/}
            {/*  </MDTypography>*/}
            {/*</MDBox>*/}
            {/*{errors.agreeError && (*/}
            {/*  <MDTypography variant="caption" color="error" fontWeight="light">*/}
            {/*    You must agree to the Terms and Conditions*/}
            {/*  </MDTypography>*/}
            {/*)}*/}
            {errors.error && (
              <MDTypography variant="caption" color="error" fontWeight="light">
                {errors.errorText}
              </MDTypography>
            )}
            <MDBox mt={4} mb={1}>
              <MDButton variant="gradient" color="info" fullWidth type="submit">
                sign in
              </MDButton>
            </MDBox>
            <MDBox mt={3} mb={1} textAlign="center">
              <MDTypography variant="button" color="text">
                Already have an account?{" "}
                <MDTypography
                  component={Link}
                  to="/auth/login"
                  variant="button"
                  color="info"
                  fontWeight="medium"
                  textGradient
                >
                  Sign In
                </MDTypography>
              </MDTypography>
            </MDBox>
          </MDBox>
        </MDBox>
      </Card>
      <MDBox mt={3} mb={1} textAlign="center">
        {successSB && (renderSuccessSB)}
      </MDBox>
    </CoverLayout>
  );
}

export default Register;
