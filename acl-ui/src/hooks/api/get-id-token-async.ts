import { useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import useAuth from "src/components/auth/hooks/auth";

export default function useGetIdTokenAsync() {
  const navigate = useNavigate();
  const location = useLocation();
  const { getIdTokenAsync } = useAuth();

  return useCallback(async () => {
    try {
      return await getIdTokenAsync();
    } catch (error: any) {
      navigate("/", { replace: true, state: { from: location, error } });
      return null;
    }
  }, [getIdTokenAsync, navigate, location]);
}
