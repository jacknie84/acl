import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { waitingAsync } from "src/utils/promise";
import BuildingForm from "./components/BuildingForm";
import { SaveBuilding } from "./types";

function BuildingRegister() {
  const navigate = useNavigate();
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const onValid = useCallback(
    async (building: SaveBuilding) => {
      setPending(true);
      try {
        await waitingAsync(1000);
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
    [publish, navigate],
  );

  return <BuildingForm isPending={isPending} submitHandlers={{ onValid }} onCancel={() => navigate(-1)}></BuildingForm>;
}

export default BuildingRegister;
