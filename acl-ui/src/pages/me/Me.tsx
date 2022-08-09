import { useQuery } from "@tanstack/react-query";
import moment from "moment";
import { useCallback, useState } from "react";
import { Button, Form, Stack } from "react-bootstrap";
import { FieldErrors, useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import LoadingButton from "src/components/LoadingButton";
import { FetchResponseError } from "src/hooks/api/api-operation";
import { PutMe, useGetMeApi, usePutMeApi } from "src/hooks/api/me";
import PasswordController from "./PasswordController";

function Me() {
  const getMeAsync = useGetMeApi();
  const putMeAsync = usePutMeApi();
  const { data: me } = useQuery(["getMe"], () => getMeAsync());
  const { control, register, handleSubmit, setError } = useForm<PutMe>();
  const navigate = useNavigate();
  const [isPending, setPending] = useState(false);
  const publish = useConfirmModal();
  const onValid = useCallback(
    async (me: PutMe) => {
      setPending(true);
      try {
        await putMeAsync(me);
        publish({
          title: "안내",
          body: "내 정보가 저장 되었습니다.",
          onConfirm: ({ close }) => {
            close();
            navigate("/home");
          },
        });
      } catch (error: any) {
        const { response } = error as FetchResponseError;
        if (response?.status === 403) {
          setError("orgPassword", { message: "자격 증명에 실패 하였습니다." }, { shouldFocus: true });
        } else {
          throw error;
        }
      } finally {
        setPending(false);
      }
    },
    [navigate, publish, putMeAsync, setError],
  );
  const onInvalid = useCallback((error: FieldErrors<PutMe>) => {
    console.log(error);
  }, []);

  return (
    <Form onSubmit={handleSubmit(onValid, onInvalid)}>
      <PasswordController
        isPending={isPending}
        control={control}
        register={register}
        label="현재 비밀번호"
        name="orgPassword"
        placeholder="현재 비밀번호를 입력 해 주세요."
      />
      <PasswordController
        isPending={isPending}
        control={control}
        register={register}
        label="새 비밀번호"
        name="newPassword"
        placeholder="새 비밀번호를 입력 해 주세요."
      />
      {me && (
        <Form.Group className="mb-3" controlId="lastModifiedDate">
          <Form.Label>마지막 수정 일시</Form.Label>
          <p>{moment(me.lastModifiedDate).format("YYYY-MM-DD HH:mm:ss")}</p>
        </Form.Group>
      )}
      <Stack direction="horizontal" gap={2}>
        <Button className="ms-auto" disabled={isPending} variant="outline-secondary" onClick={() => navigate("/home")}>
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
