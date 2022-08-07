import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { waitingAsync } from "src/utils/promise";
import BuildingFacilityForm from "./components/BuildingFacilityForm";
import { SaveBuildingFacility } from "./types";

function BuildingFacilityRegister() {
  const navigate = useNavigate();
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const onValid = useCallback(
    async (facility: SaveBuildingFacility) => {
      setPending(true);
      try {
        await waitingAsync(1000);
      } finally {
        setPending(false);
      }
      publish({
        title: "안내",
        body: "시설이 생성 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate(-1);
        },
      });
    },
    [publish, navigate],
  );

  return (
    <BuildingFacilityForm isPending={isPending} submitHandlers={{ onValid }} onCancel={() => navigate(-1)}></BuildingFacilityForm>
  );
}

export default BuildingFacilityRegister;
