import { computed, Ref, toRefs, useContext } from "@nuxtjs/composition-api";
import { User } from "~/models";

export function useAuth() {
  const { $auth } = useContext()

  const user = computed(() => $auth.user)
  const loggedIn = computed(() => $auth.loggedIn)

  return {
    user,
    loggedIn,
  } as {
    user: Ref<User | null>,
    loggedIn: Ref<boolean>
  }
}