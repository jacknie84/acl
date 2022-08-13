import { useCallback } from "react";
import { Button, Form, Stack } from "react-bootstrap";
import { FieldErrors, useForm } from "react-hook-form";
import LoadingButton from "src/components/LoadingButton";
import { MemberAccount, useDeleteMemberAccountApi } from "src/hooks/api/member-account";
import useDeleteEventListener from "src/hooks/delete-event-listener";
import EmailController from "./EmailController";
import PasswordController from "./PasswordController";
import RolesController from "./RolesController";

type Props = {
  member?: Partial<MemberAccount>;
  isPending: boolean;
  submitHandlers: {
    onValid: (member: Partial<MemberAccount>) => void;
    onInvalid?: (error: FieldErrors<Partial<MemberAccount>>) => void;
  };
  onCancel: () => void;
  onDelete?: () => void;
};

function MemberForm({
  member = { roles: ["ROLE_USER"] },
  isPending,
  submitHandlers: { onValid: onValidInternal, onInvalid = () => {} },
  onCancel,
  onDelete,
}: Props) {
  const { control, register, handleSubmit, setError } = useForm<Partial<MemberAccount>>({
    defaultValues: member,
  });
  const deleteMemberAccountAsync = useDeleteMemberAccountApi(member.id as number);
  const onClickDelete = useDeleteEventListener(async () => await deleteMemberAccountAsync(), onDelete);
  const onValid = useCallback(
    async (member: Partial<MemberAccount>) => {
      try {
        await onValidInternal(member);
      } catch (error: any) {
        if (!("code" in error)) {
          throw error;
        }
        setError("email", { message: "이미 사용 중인 이메일 입니다." });
      }
    },
    [onValidInternal, setError],
  );

  return (
    <Form onSubmit={handleSubmit(onValid, onInvalid)}>
      <EmailController isPending={isPending} control={control} register={register} />
      <PasswordController isPending={isPending} control={control} register={register} />
      <RolesController isPending={isPending} control={control} />
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
