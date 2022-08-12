import { useQuery } from "@tanstack/react-query";
import { useCallback, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { MemberAccount, useGetMemberAccountApi, usePutMemberAccountApi } from "src/hooks/api/member-account";
import MemberForm from "./components/MemberForm";

type Params = { memberId: string };

function MemberDetail() {
  const navigate = useNavigate();
  const { memberId } = useParams<Params>();
  const id = useMemo(() => parseInt(memberId!), [memberId]);
  const getMemberAccountAsync = useGetMemberAccountApi(id);
  const putMemberAccountAsync = usePutMemberAccountApi(id);
  const { data: member } = useQuery(["getMemberAccount"], () => getMemberAccountAsync());
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const onValid = useCallback(
    async (member: Partial<MemberAccount>) => {
      setPending(true);
      try {
        await putMemberAccountAsync(member);
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
    [publish, navigate, putMemberAccountAsync],
  );

  return (
    <>
      {member && (
        <MemberForm
          member={member}
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
