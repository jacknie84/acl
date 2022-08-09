import { useCallback, useEffect, useMemo, useState } from "react";
import useGetIdTokenAsync from "./get-id-token-async";
import { FetchResponseError, Me } from "./type";

export default function useGetMeApi() {
  const [value, setValue] = useState<Me>();
  const [error, setError] = useState<any>();
  const getIdTokenAsync = useGetIdTokenAsync();
  const getMeAsync = useCallback(async () => {
    const idToken = await getIdTokenAsync();
    if (!idToken) {
      return;
    }
    const headers = { "Content-Type": "application/json", Authorization: `Bearer ${idToken}` };
    const response = await fetch("http://localhost:8080/me", { method: "get", headers });
    if (response.ok) {
      return await response.json();
    } else {
      throw new FetchResponseError(response, "GET /me API 호출 도중 에러");
    }
  }, [getIdTokenAsync]);

  useEffect(() => {
    getMeAsync().then(setValue).catch(setError);
  }, [getMeAsync]);

  return useMemo(() => ({ value, error }), [value, error]);
}
