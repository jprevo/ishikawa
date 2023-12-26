import { createContext } from "react";
import { User } from "./user.ts";

interface UserContextApi {
  user: User | undefined;
  setUser: CallableFunction;
  login: CallableFunction;
  logout: CallableFunction;
}

const UserContext = createContext<UserContextApi>({
  user: undefined,
  setUser: () => {},
  login: () => {},
  logout: () => {},
});

export default UserContext;
