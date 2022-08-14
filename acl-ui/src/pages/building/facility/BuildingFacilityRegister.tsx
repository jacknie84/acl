import { useCallback, useContext, useState } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { SaveBuildingFacility, usePostBuildingFacilityApi } from "src/hooks/api/building-facility";
import BuildingFacilityForm from "./components/BuildingFacilityForm";
import BuildingFacilityContext from "./contexts/BuildingFacilityContext";

function BuildingFacilityRegister() {
  const navigate = useNavigate();
  const { buildingId } = useParams();
  const [searchParams] = useSearchParams();
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const { facilityPath } = useContext(BuildingFacilityContext);
  const current = facilityPath[facilityPath.length - 1];
  const postBuildingFacility = usePostBuildingFacilityApi(buildingId!);
  const onValid = useCallback(
    async (facility: SaveBuildingFacility) => {
      setPending(true);
      try {
        await postBuildingFacility({ ...facility, parentId: current?.id });
      } finally {
        setPending(false);
      }
      publish({
        title: "안내",
        body: "시설이 생성 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate(`../tabs/list?${searchParams}`);
        },
      });
    },
    [publish, navigate, postBuildingFacility, searchParams, current],
  );

  return (
    <BuildingFacilityForm
      isPending={isPending}
      submitHandlers={{ onValid }}
      onCancel={() => navigate(`../tabs/list?${searchParams}`)}
    ></BuildingFacilityForm>
  );
}

export default BuildingFacilityRegister;
