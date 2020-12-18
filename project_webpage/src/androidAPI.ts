export interface androidAPI {
  getRoot(): string
  getRootFullPath(): string
  listFile(path: string): string
  setCurrentPath(path: string): void
}
