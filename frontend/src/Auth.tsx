import {useEffect, useState} from "react";
import {User} from "./user.ts";

function Auth() {
    const [user, setUser] = useState<User>();

    useEffect(() => {
        const loadUser = async () => {
            const response: Response = await fetch("/user");
            const data: User | null = await response.json();

            setUser(data);
        }

        loadUser();
    }, [setUser]);

    if (user === undefined) {
        return <>You are not logged in.</>;
    }

    return <>Hello, {user.username}</>
}

export default Auth;