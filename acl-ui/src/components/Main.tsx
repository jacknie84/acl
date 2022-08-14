import { useCallback, useEffect } from "react";
import { Navigate, Outlet, Route, Routes, useLocation, useNavigate } from "react-router-dom";
import BuildingRegister from "src/pages/building/BuildingRegister";
import Buildings from "src/pages/building/Buildings";
import BuildingTabs from "src/pages/building/BuildingTabs";
import BuildingDetail from "src/pages/building/BuildingTabs/BuildingDetail";
import BuildingFacilities from "src/pages/building/facility/BuildingFacilities";
import BuildingFacilityDetail from "src/pages/building/facility/BuildingFacilityDetail";
import BuildingFacilityRegister from "src/pages/building/facility/BuildingFacilityRegister";
import BuildingFacilityTabs from "src/pages/building/facility/BuildingFacilityTabs";
import FacilityPage from "src/pages/building/facility/FacilityPage";
import Me from "src/pages/me/Me";
import MemberRegister from "src/pages/member/MemberRegister";
import appStateStore from "src/utils/app-state-store";
import Dashboard from "../pages/Dashboard";
import GettingStart from "../pages/GettingStart";
import MemberDetail from "../pages/member/MemberDetail";
import Members from "../pages/member/Members";
import { AuthProvider } from "./auth";
import { ConfirmProvider } from "./confirm";
import Layout from "./Layout";
import PageView from "./PageView";

type ReturnState = { from?: Location; error?: any };

function Main() {
  const location = useLocation();
  const navigate = useNavigate();
  const onAuthenticated = useCallback(async () => {
    const { returnTo = { uri: "/home" } } = await appStateStore.loadAsync();
    navigate(returnTo.uri, { replace: true });
  }, [navigate]);
  const onError = useCallback((error: any) => console.log(error), []);

  useEffect(() => {
    const { from, error } = (location.state ?? {}) as ReturnState;
    if (from) {
      const appState = { returnTo: { uri: `${from.pathname}${from.search}`, reason: error?.message } };
      appStateStore.saveAsync(appState);
    }
  }, [location]);

  return (
    <AuthProvider onAuthenticated={onAuthenticated} onError={onError}>
      <ConfirmProvider>
        <Routes>
          <Route path="/" element={<Outlet />}>
            <Route path="" element={<Navigate to="/getting-start" replace />} />
            <Route path="callback" element={<Outlet />} />
            <Route path="getting-start" element={<GettingStart />} />
            <Route element={<Layout />}>
              <Route path="home" element={<PageView title="대시보드" />}>
                <Route path="" element={<Dashboard />} />
              </Route>
              <Route path="members" element={<PageView title="회원 관리" />}>
                <Route path="" element={<Members />} />
                <Route path="form" element={<MemberRegister />} />
                <Route path=":memberId/form" element={<MemberDetail />} />
              </Route>
              <Route path="buildings" element={<PageView title="건물 관리" />}>
                <Route path="" element={<Buildings />} />
                <Route path="form" element={<BuildingRegister />} />
                <Route path=":buildingId">
                  <Route path="" element={<Navigate to="tabs" />} />
                  <Route path="tabs" element={<BuildingTabs />}>
                    <Route path="" element={<Navigate to="form" replace />} />
                    <Route path="form" element={<BuildingDetail />} />
                    <Route path="facility" element={<FacilityPage />}>
                      <Route path="" element={<Navigate to="tabs" replace />} />
                      <Route path="tabs" element={<BuildingFacilityTabs />}>
                        <Route path="" element={<Navigate to="list" replace />} />
                        <Route path="list" element={<BuildingFacilities />} />
                        <Route path="detail" element={<BuildingFacilityDetail />} />
                      </Route>
                      <Route path="form" element={<BuildingFacilityRegister />} />
                    </Route>
                  </Route>
                </Route>
              </Route>
              <Route path="me" element={<Me />} />
            </Route>
          </Route>
        </Routes>
      </ConfirmProvider>
    </AuthProvider>
  );
}

export default Main;
