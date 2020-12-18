import { androidAPI } from "./androidAPI"

class MockAPI implements androidAPI {
  getRoot() {
    return "example"
  }
  getRootFullPath() {
    return "/root/example"
  }
  listFile(path: string): string {
    return JSON.stringify([
      { name: "dirrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr", leaf: false },
      { name: "file", leaf: true },
    ]);
  }
  setCurrentPath(path: string) {
    console.log(path)
  }
}

export const API = new MockAPI();