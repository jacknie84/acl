import { Button, Form, Stack } from "react-bootstrap";
import { FieldErrors, useForm } from "react-hook-form";
import LoadingButton from "src/components/LoadingButton";
import useDeleteEventListener from "src/hooks/delete-event-listener";
import { waitingAsync } from "src/utils/promise";
import { SaveMember } from "../../types";
import EmailController from "./EmailController";
import PasswordController from "./PasswordController";
import RolesController from "./RolesController";

type Props = {
  member?: SaveMember;
  isPending: boolean;
  submitHandlers: { onValid: (member: SaveMember) => void; onInvalid?: (error: FieldErrors<SaveMember>) => void };
  onCancel: () => void;
  onDelete?: () => void;
};

function MemberForm({ member = {}, isPending, submitHandlers: { onValid, onInvalid = () => {} }, onCancel, onDelete }: Props) {
  const { control, register, handleSubmit } = useForm<SaveMember>({ defaultValues: member });
  const onClickDelete = useDeleteEventListener(() => waitingAsync(1000), onDelete);

  return (
    <Form onSubmit={handleSubmit(onValid, onInvalid)}>
      <EmailController isPending={isPending} control={control} register={register} />
      <PasswordController isPending={isPending} control={control} register={register} />
      <RolesController isPending={isPending} control={control} register={register} />
      <Stack direction="horizontal" gap={2}>
        {member.id && (
          <Button variant="danger" disabled={isPending} onClick={onClickDelete}>
            삭제
          </Button>
        )}
        <Button className="ms-auto" disabled={isPending} variant="outline-secondary" onClick={onCancel}>
          취소
        </Button>
        <LoadingButton type="submit" isLoading={isPending}>
          저장
        </LoadingButton>
      </Stack>
    </Form>
  );
}

export default MemberForm;
