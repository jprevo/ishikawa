import {useState} from "react";
import UserContext from "./user-context.ts";

const UserProvider = ({ children }) => {
    const [user, setUser] = useState();

    const login = () => {
        window.location.href = "/login";
    }

    const logout = () => {
        window.location.href = "/logout";
    }

    return (
        <UserContext.Provider value={{ user, setUser, login, logout }}>
            {children}
        </UserContext.Provider>
    );
}

export default UserProvider;