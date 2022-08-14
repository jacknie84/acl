import { useQuery } from "@tanstack/react-query";
import { useCallback, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { MemberAccount, useGetMemberAccountApi, usePutMemberAccountApi } from "src/hooks/api/member-account";
import MemberForm from "./components/MemberForm";

type Params = { memberId: string };

function MemberDetail() {
  const navigate = useNavigate();
  const { memberId } = useParams<Params>();
  const getMemberAccountAsync = useGetMemberAccountApi(memberId!);
  const putMemberAccountAsync = usePutMemberAccountApi(memberId!);
  const { data, refetch } = useQuery(["getMemberAccount", memberId], () => getMemberAccountAsync());
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const onValid = useCallback(
    async (member: Partial<MemberAccount>) => {
      setPending(true);
      try {
        await putMemberAccountAsync(member);
        await refetch();
      } finally {
        setPending(false);
      }
      publish({
        title: "안내",
        body: "회원 정보가 수정 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate("..");
        },
      });
    },
    [publish, navigate, putMemberAccountAsync, refetch],
  );

  return (
    <>
      {data && (
        <MemberForm
          member={data}
          isPending={isPending}
          submitHandlers={{ onValid }}
          onDelete={() => {
            publish({
              title: "안내",
              body: "회원 정보가 삭제 되었습니다.",
              onConfirm: ({ close }) => {
                close();
                navigate("..");
              },
            });
          }}
          onCancel={() => navigate("..")}
        />
      )}
    </>
  );
}

export default MemberDetail;
