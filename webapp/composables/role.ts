import { Oauth2Scheme } from "@nuxtjs/auth-next";
import { useContext } from "@nuxtjs/composition-api";
import { fixedComputed } from "./hack";

export function hasRole(role: "admin") {
  const { $auth } = useContext()

  return fixedComputed(() => {
    const scheme = $auth.strategy as Oauth2Scheme
    const token = scheme.token.get() as string

    if (!token) {
      return false
    }

    const decoded = Buffer.from(token.split(".")[1], 'base64').toString();
    const body = JSON.parse(decoded)
    const roles = body["realm_access"]["roles"] as Array<string>

    return roles.includes(role)
  })
}