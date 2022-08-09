export type AppState = { returnTo?: { uri: string; reason?: string } };

const appStateStore = {
  async saveAsync(state: AppState) {
    sessionStorage.setItem("appState", JSON.stringify(state));
  },
  async loadAsync() {
    const stringified = sessionStorage.getItem("appState");
    if (stringified) {
      return JSON.parse(stringified) as AppState;
    } else {
      return {} as AppState;
    }
  },
  clear() {
    sessionStorage.removeItem("appState");
  },
};

export default appStateStore;
