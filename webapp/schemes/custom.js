import { OpenIDConnectScheme } from "~auth/runtime"

export default class CustomScheme extends OpenIDConnectScheme {

  async fetchUser() {
    if (!this.check().valid) {
      return;
    }

    if (this.options.endpoints.userInfo) {
      const { data } = await this.$auth.requestWith(this.name, {
        url: this.options.endpoints.userInfo
      });
      this.$auth.setUser(data);
      return;
    }

    if (this.idToken.get()) {
      const data2 = this.idToken.userInfo();
      this.$auth.setUser(data2);
      return;
    }

    this.$auth.setUser({});
  }

  // window.crypto is not available on the server side
  generateRandomString() {
    return Array(28)
      .fill(0)
      .map(() => ('0' + parseInt(Math.random() * 100000000000000).toString(16)).substr(-2))
      .join('');
  }

}