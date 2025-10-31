import React from "react";
import ReactDOM, { createRoot } from "react-dom/client";
import App from "./App";
import { keycloakInstance } from "./keycloak";
import { BrowserRouter } from "react-router-dom";

const startApp = () => {
  createRoot(document.getElementById("root")!).render(<App />);
};

keycloakInstance.connect().then(startApp).catch(console.error);
