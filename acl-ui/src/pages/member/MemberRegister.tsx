import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { waitingAsync } from "src/utils/promise";
import MemberForm from "./components/MemberForm";
import { SaveMember } from "./types";

function MemberRegister() {
  const navigate = useNavigate();
  const publish = useConfirmModal();
  const [isPending, setPending] = useState(false);
  const onValid = useCallback(
    async (member: SaveMember) => {
      setPending(true);
      try {
        await waitingAsync(1000);
      } finally {
        setPending(false);
      }
      publish({
        title: "안내",
        body: "회원이 생성 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate(-1);
        },
      });
    },
    [publish, navigate],
  );

  return <MemberForm isPending={isPending} submitHandlers={{ onValid }} onCancel={() => navigate(-1)} />;
}

export default MemberRegister;
