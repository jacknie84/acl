import { OAuth2Error, OAuth2TokenResponseBody } from "../types";

const storage = sessionStorage;

const tokenStore = {
  async saveAsync(token: OAuth2TokenResponseBody) {
    storage.setItem("tokenResponse", JSON.stringify(token));
    return token;
  },
  clear() {
    storage.removeItem("tokenResponse");
  },
  async loadAsync() {
    const tokenResponse = storage.getItem("tokenResponse");
    if (tokenResponse) {
      return JSON.parse(tokenResponse) as OAuth2TokenResponseBody;
    } else {
      throw new OAuth2Error("토큰 저장소에서 토큰 정보를 찾을 수 없습니다.");
    }
  },
};

export default tokenStore;
