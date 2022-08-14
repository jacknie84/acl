import { useQuery } from "@tanstack/react-query";
import { useCallback, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { SaveBuilding, useGetBuildingApi, usePutBuildingApi } from "src/hooks/api/building";
import BuildingForm from "../components/BuildingForm";

function BuildingDetail() {
  const navigate = useNavigate();
  const { buildingId } = useParams();
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const getBuildingAsync = useGetBuildingApi(buildingId!);
  const putBuildingAsync = usePutBuildingApi(buildingId!);
  const { data, refetch } = useQuery(["getBuilding", buildingId], () => getBuildingAsync());
  const onValid = useCallback(
    async (building: SaveBuilding) => {
      setPending(true);
      try {
        await putBuildingAsync(building);
        await refetch();
      } finally {
        setPending(false);
      }
      publish({
        title: "안내",
        body: "건물 정보가 수정 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate("../../..");
        },
      });
    },
    [publish, navigate, putBuildingAsync, refetch],
  );

  return (
    <>
      {data && (
        <BuildingForm
          building={data}
          isPending={isPending}
          submitHandlers={{ onValid }}
          onDelete={() => {
            publish({
              title: "안내",
              body: "건물 정보가 삭제 되었습니다.",
              onConfirm: ({ close }) => {
                close();
                navigate("../../..");
              },
            });
          }}
          onCancel={() => navigate("../../..")}
        />
      )}
    </>
  );
}

export default BuildingDetail;
