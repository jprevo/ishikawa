import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import "./scss/index.scss";
import UserProvider from "./user/UserProvider.tsx";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <UserProvider>
      <div data-theme="dark" id="main">
        <App />
      </div>
    </UserProvider>
  </React.StrictMode>,
);
