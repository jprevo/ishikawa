import "./scss/login.scss";
import { useContext, useEffect } from "react";
import UserContext from "./user/user-context.ts";
import { User } from "./user/user.ts";

function App() {
  const { user, setUser, login, logout } = useContext(UserContext);

  useEffect(() => {
    const loadUser = async () => {
      const response: Response = await fetch("/user");
      const data: User | null = await response.json();

      if (!data) {
        return setUser(undefined);
      }

      setUser(data);
    };

    loadUser();
  }, [setUser, login]);

  const onLoginClick = () => {
    login();
  };

  const onLogoutClick = () => {
    logout();
  };

  if (user === undefined) {
    return (
      <div className="login-screen">
        <h1>Ishikawa Bot</h1>
        <p>Vous n'êtes pas connecté·e.</p>
        <p>
          <a onClick={onLoginClick} href="#" role="button">
            Se connecter
          </a>
        </p>
      </div>
    );
  }

  return (
    <>
      {user.username}. <a onClick={onLogoutClick}>Logout</a>{" "}
    </>
  );
}

export default App;
