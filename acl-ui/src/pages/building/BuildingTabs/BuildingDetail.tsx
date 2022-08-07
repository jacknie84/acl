import { useCallback, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { waitingAsync } from "src/utils/promise";
import BuildingForm from "../components/BuildingForm";
import { SaveBuilding } from "../types";

const building = { name: "꼬마빌딩(50억)", lastModifiedDate: new Date().toJSON() } as SaveBuilding;

function BuildingDetail() {
  const navigate = useNavigate();
  const { buildingId } = useParams();
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
        body: "건물 정보가 수정 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate(-1);
        },
      });
    },
    [publish, navigate],
  );

  return (
    <BuildingForm
      building={{ id: buildingId, ...building }}
      isPending={isPending}
      submitHandlers={{ onValid }}
      onDelete={() => {
        publish({
          title: "안내",
          body: "건물 정보가 삭제 되었습니다.",
          onConfirm: ({ close }) => {
            close();
            navigate(-1);
          },
        });
      }}
      onCancel={() => navigate(-1)}
    />
  );
}

export default BuildingDetail;
