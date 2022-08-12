import { useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import useAuth from "src/components/auth/hooks/auth";

export default function useGetIdTokenAsync() {
  const navigate = useNavigate();
  const location = useLocation();
  const { getIdTokenAsync } = useAuth();

  return useCallback(
    async (forceRefreshToken?: boolean) => {
      try {
        return await getIdTokenAsync(forceRefreshToken);
      } catch (error: any) {
        navigate("/", { replace: true, state: { from: location, error } });
        throw error;
      }
    },
    [getIdTokenAsync, navigate, location],
  );
}
