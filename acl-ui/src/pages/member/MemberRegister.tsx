import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { MemberAccount, usePostMemberAccountApi } from "src/hooks/api/member-account";
import MemberForm from "./components/MemberForm";

function MemberRegister() {
  const navigate = useNavigate();
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const createMemberAccountAsync = usePostMemberAccountApi();
  const onValid = useCallback(
    async (member: Partial<MemberAccount>) => {
      setPending(true);
      try {
        await createMemberAccountAsync(member);
      } finally {
        setPending(false);
      }
      publish({
        title: "안내",
        body: "회원이 생성 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate("..");
        },
      });
    },
    [publish, navigate, createMemberAccountAsync],
  );

  return <MemberForm isPending={isPending} submitHandlers={{ onValid }} onCancel={() => navigate("..")} />;
}

export default MemberRegister;
