import './App.scss'
import {useContext, useEffect} from "react";
import UserContext from "./user-context.ts";
import {User} from "./user.ts";

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
        }

        loadUser();
    }, [setUser, login]);

    const onLoginClick = () => {
        login();
    }

    const onLogoutClick = () => {
        logout();
    }

    if (user === undefined) {
        return <>Vous n'êtes pas connecté. <a onClick={onLoginClick}>Se connecter.</a></>;
    }

    return <>Wesh, {user.username}. <a onClick={onLogoutClick}>Logout</a> </>
}

export default App
