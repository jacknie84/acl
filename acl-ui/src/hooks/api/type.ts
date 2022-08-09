export type Me = { email: string; lastModifiedDate: string };

export class FetchResponseError extends Error {
  response: Response;

  constructor(response: Response, message?: string) {
    super(message);
    this.response = response;
  }
}
