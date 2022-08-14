import { Button, Form, Stack } from "react-bootstrap";
import { FieldErrors, useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import LoadingButton from "src/components/LoadingButton";
import { SaveBuildingFacility, useDeleteBuildingFacilityApi } from "src/hooks/api/building-facility";
import useDeleteEventListener from "src/hooks/delete-event-listener";
import InheritingController from "./acl/InheritingController";
import OwnerController from "./acl/OwnerController";
import NameController from "./NameController";

type Props = {
  facility?: SaveBuildingFacility;
  isPending: boolean;
  submitHandlers: {
    onValid: (facility: SaveBuildingFacility) => void;
    onInvalid?: (error: FieldErrors<SaveBuildingFacility>) => void;
  };
  onCancel: () => void;
  onDelete?: () => void;
};

const defaultFacility = { acl: { inheriting: false } } as SaveBuildingFacility;

function BuildingFacilityForm({
  facility = defaultFacility,
  isPending,
  submitHandlers: { onValid, onInvalid },
  onCancel,
  onDelete,
}: Props) {
  const { control, register, handleSubmit, watch } = useForm<SaveBuildingFacility>({ defaultValues: facility });
  const { buildingId } = useParams();
  const deleteFacilityAsync = useDeleteBuildingFacilityApi(buildingId!, facility.id!);
  const onClickDelete = useDeleteEventListener(deleteFacilityAsync, onDelete);

  return (
    <Form onSubmit={handleSubmit(onValid, onInvalid)}>
      <NameController isPending={isPending} register={register} control={control} />
      <OwnerController isPending={isPending} register={register} control={control} />
      <InheritingController isPending={isPending} control={control} />
      {/* <EntriesController isPending={isPending} register={register} control={control} isInheriting={watch().acl?.inheriting} /> */}
      <Stack className="mt-3" direction="horizontal" gap={2}>
        {facility.id && (
          <Button disabled={isPending} variant="danger" onClick={onClickDelete}>
            삭제
          </Button>
        )}
        <Button disabled={isPending} className="ms-auto" variant="outline-secondary" onClick={onCancel}>
          취소
        </Button>
        <LoadingButton type="submit" isLoading={isPending}>
          저장
        </LoadingButton>
      </Stack>
    </Form>
  );
}

export default BuildingFacilityForm;
