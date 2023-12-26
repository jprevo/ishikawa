import "./scss/nav.scss";
import { useContext } from "react";
import UserContext from "./user/user-context.ts";

function Nav() {
  const { user, logout } = useContext(UserContext);

  const onLogoutClick = () => {
    logout();
  };

  return (
    <nav className="container">
      <ul>
        <li>
          <h2 className="nav-header">Ishikawa</h2>
        </li>
      </ul>
      <ul>
        <li>{user?.username}</li>
        <li>
          <a href="#" onClick={onLogoutClick}>
            Déconnexion
          </a>
        </li>
      </ul>
    </nav>
  );
}

export default Nav;
