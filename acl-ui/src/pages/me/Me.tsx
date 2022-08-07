import { useCallback, useState } from "react";
import { Button, Form, Stack } from "react-bootstrap";
import { FieldErrors, useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import LoadingButton from "src/components/LoadingButton";
import { waitingAsync } from "src/utils/promise";
import EmailController from "../member/components/MemberForm/EmailController";
import PasswordController from "../member/components/MemberForm/PasswordController";
import { SaveMember } from "../member/types";

function Me() {
  const { control, register, handleSubmit } = useForm<SaveMember>({
    defaultValues: { email: "test@email.com", password: "123456" },
  });
  const navigate = useNavigate();
  const [isPending, setPending] = useState(false);
  const publish = useConfirmModal();
  const onValid = useCallback(
    async (member: SaveMember) => {
      setPending(true);
      await waitingAsync(1000);
      setPending(false);
      publish({
        title: "안내",
        body: "내 정보가 저장 되었습니다.",
        onConfirm: ({ close }) => close(),
      });
    },
    [publish],
  );
  const onInvalid = useCallback((error: FieldErrors<SaveMember>) => {}, []);

  return (
    <Form onSubmit={handleSubmit(onValid, onInvalid)}>
      <EmailController isPending={isPending} control={control} register={register} />
      <PasswordController isPending={isPending} control={control} register={register} />
      <Stack direction="horizontal" gap={2}>
        <Button className="ms-auto" disabled={isPending} variant="outline-secondary" onClick={() => navigate("../home")}>
          홈 으로 가기
        </Button>
        <LoadingButton type="submit" isLoading={isPending}>
          저장
        </LoadingButton>
      </Stack>
    </Form>
  );
}

export default Me;
