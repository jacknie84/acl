import { useCallback, useContext, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { waitingAsync } from "src/utils/promise";
import BuildingContext from "../contexts/BuildingContext";
import BuildingFacilityForm from "./components/BuildingFacilityForm";
import { SaveBuildingFacility } from "./types";

function BuildingFacilityDetail() {
  const navigate = useNavigate();
  const { facilityPath, facilities } = useContext(BuildingContext);
  const facility = useMemo<SaveBuildingFacility>(() => {
    if (facilityPath.length > 0) {
      const id = facilityPath[facilityPath.length - 1];
      return facilities[id];
    } else {
      return {};
    }
  }, [facilityPath, facilities]);
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
        body: "시설 정보가 수정 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate(-1);
        },
      });
    },
    [publish, navigate],
  );

  return (
    <BuildingFacilityForm
      facility={{ id: facility.id, ...facility }}
      isPending={isPending}
      submitHandlers={{ onValid }}
      onDelete={() => {
        publish({
          title: "안내",
          body: "시설 정보가 삭제 되었습니다.",
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

export default BuildingFacilityDetail;
