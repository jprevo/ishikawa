import {PropsWithChildren, useState} from "react";
import UserContext from "./user-context.ts";
import {User} from "./user.ts";

function UserProvider(props: PropsWithChildren) {
    const [user, setUser] = useState<User>();

    const login = () => {
        window.location.href = "/login";
    }

    const logout = () => {
        window.location.href = "/logout";
    }

    return (
        <UserContext.Provider value={{ user, setUser, login, logout }}>
            {props.children}
        </UserContext.Provider>
    );
}

export default UserProvider;