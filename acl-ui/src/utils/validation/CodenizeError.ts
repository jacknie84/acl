export default class CodenizeError<T = string> extends Error {
  code: T;

  constructor(code: T, message?: string, options?: ErrorOptions) {
    super(message ?? `${code}`, options);
    this.code = code;
  }
}
