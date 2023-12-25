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

    if (user === undefined) {
        return <>Vous n'êtes pas connecté. <a onClick={login}>Se connecter.</a></>;
    }

    return <>Wesh, {user.username}. <a onClick={logout}>Logout</a> </>
}

export default App
