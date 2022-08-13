import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { SaveBuilding, usePostBuildingApi } from "src/hooks/api/building";
import BuildingForm from "./components/BuildingForm";

function BuildingRegister() {
  const navigate = useNavigate();
  const publish = useConfirmModal();
  const postBuildingAsync = usePostBuildingApi();
  const [isPending, setPending] = useState(false);
  const onValid = useCallback(
    async (building: SaveBuilding) => {
      setPending(true);
      try {
        await postBuildingAsync(building);
      } finally {
        setPending(false);
      }
      publish({
        title: "안내",
        body: "건물이 생성 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate(-1);
        },
      });
    },
    [publish, navigate, postBuildingAsync],
  );

  return <BuildingForm isPending={isPending} submitHandlers={{ onValid }} onCancel={() => navigate("..")}></BuildingForm>;
}

export default BuildingRegister;
