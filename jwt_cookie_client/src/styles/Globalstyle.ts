import { createGlobalStyle } from "styled-components";

const GlobalStyle = createGlobalStyle`

  html, body {
    box-sizing: border-box;
  }

  *,
  *::before,
  *::after {
    box-sizing: inherit;
  }

  #root{
    height: 100%;
    margin: 0;
    padding: 0; 
  }

  html {
    font-size: 16px;
    height: 100%;
    margin: 0;
    padding: 0;
  }

  body {
    font-size: 1.6rem !important;
    height: 100%;
    margin: 0;
    padding: 0;
    background: ${(props) => props.theme.palette.background.default};
    color: ${(props) => props.theme.palette.text.primary};
  }

  a{
    text-decoration: none;
    color: ${(props) => props.theme.palette.text.primary};
    font-weight: bold;
  }

  table, tr, td, th, a, div {
    font-size: 1.2rem !important;
  }

  span {
    font-size: 1rem !important;
  }

  :lang(ko) {
    h1, h2, h3 {
      word-break: keep-all;
    }
  }

  * {
    font-family: "Gothic A1", serif;
  }

`;

export default GlobalStyle;
