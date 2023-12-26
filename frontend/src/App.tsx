import "./scss/login.scss";
import { useContext, useEffect } from "react";
import UserContext from "./user/user-context.ts";
import { User } from "./user/user.ts";
import Nav from "./Nav.tsx";
import MemberList from "./MemberList.tsx";

function App() {
  const { user, setUser, login } = useContext(UserContext);

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
  }, [setUser]);

  const onLoginClick = () => {
    login();
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
      <Nav />
      <MemberList />
    </>
  );
}

export default App;
