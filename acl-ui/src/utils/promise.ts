export function waitingAsync(milliseconds: number) {
  return new Promise<void>((resolve) => setTimeout(() => resolve(), milliseconds));
}

export function waitingErrorAsync(milliseconds: number, error: any) {
  return new Promise<void>((_, reject) => setTimeout(() => reject(error), milliseconds));
}
