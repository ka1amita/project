import bgImage from "../../assets/images/bg-sign-up-cover.jpeg";
import MDBox from "../../components/MDBox";
import MDTypography from "../../components/MDTypography";
import BasicLayoutLanding from "../authentication/components/BasicLayoutLanding";
import React from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

function PageNotFound() {
    return (
        <BasicLayoutLanding image={bgImage}>
            <MDBox
                sx={{width: "auto"}}
                variant="gradient"
                bgColor="error"
                borderRadius="lg"
                coloredShadow="error"
                mx={2}
                mt={2}
                p={2}
                mb={6}
                textAlign="center"
            >
                <MDTypography variant="h1" fontWeight="medium" color="white" mt={1}>
                    Oops! You seem to be lost.
                </MDTypography>
            </MDBox>
            <Container>
                <img src={require('../../assets/images/gifs/404.gif')} alt="404-gif" width={"100%"}/>
            </Container>
        </BasicLayoutLanding>
    );
}

export default PageNotFound;
