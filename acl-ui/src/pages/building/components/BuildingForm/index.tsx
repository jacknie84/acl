import { Button, Form, Stack } from "react-bootstrap";
import { FieldErrors, useForm } from "react-hook-form";
import LoadingButton from "src/components/LoadingButton";
import useDeleteEventListener from "src/hooks/delete-event-listener";
import { waitingAsync } from "src/utils/promise";
import { SaveBuilding } from "../../types";
import NameController from "./NameController";

type Props = {
  building?: SaveBuilding;
  isPending: boolean;
  submitHandlers: { onValid: (building: SaveBuilding) => void; onInvalid?: (error: FieldErrors<SaveBuilding>) => void };
  onCancel: () => void;
  onDelete?: () => void;
};

function BuildingForm({
  building = {},
  isPending,
  submitHandlers: { onValid, onInvalid = () => {} },
  onCancel,
  onDelete,
}: Props) {
  const { control, register, handleSubmit } = useForm<SaveBuilding>({ defaultValues: building });
  const onClickDelete = useDeleteEventListener(() => waitingAsync(1000), onDelete);

  return (
    <Form onSubmit={handleSubmit(onValid, onInvalid)}>
      <NameController isPending={isPending} control={control} register={register} />
      <Stack className="mt-3" direction="horizontal" gap={2}>
        {building.id && (
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

export default BuildingForm;
