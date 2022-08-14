import { useCallback, useContext, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { SaveBuildingFacility, usePutBuildingFacilityApi } from "src/hooks/api/building-facility";
import BuildingFacilityForm from "./components/BuildingFacilityForm";
import BuildingFacilityContext from "./contexts/BuildingFacilityContext";

function BuildingFacilityDetail() {
  const navigate = useNavigate();
  const { facilityPath } = useContext(BuildingFacilityContext);
  const facility = facilityPath[facilityPath.length - 1];
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const putFacilityAsync = usePutBuildingFacilityApi(facility.buildingId, facility.id);
  const toList = useMemo(() => {
    const ids = facilityPath.map(({ id }) => id).slice(0, facilityPath.length - 1);
    const params = new URLSearchParams({ facilityPath: JSON.stringify(ids) });
    return `../../tabs/list?${params}`;
  }, [facilityPath]);
  const onValid = useCallback(
    async (facility: SaveBuildingFacility) => {
      setPending(true);
      try {
        await putFacilityAsync(facility);
      } finally {
        setPending(false);
      }
      publish({
        title: "안내",
        body: "시설 정보가 수정 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate(toList);
        },
      });
    },
    [publish, navigate, putFacilityAsync, toList],
  );

  return (
    <>
      {facility && (
        <BuildingFacilityForm
          facility={facility}
          isPending={isPending}
          submitHandlers={{ onValid }}
          onDelete={() => {
            publish({
              title: "안내",
              body: "시설 정보가 삭제 되었습니다.",
              onConfirm: ({ close }) => {
                close();
                navigate(toList);
              },
            });
          }}
          onCancel={() => navigate(toList)}
        />
      )}
    </>
  );
}

export default BuildingFacilityDetail;
