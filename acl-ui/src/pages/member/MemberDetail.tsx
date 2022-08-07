import { useCallback, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useConfirmModal } from "src/components/confirm";
import { waitingAsync } from "src/utils/promise";
import MemberForm from "./components/MemberForm";
import { MemberRole, SaveMember } from "./types";

type Params = { memberId: string };

const member = { email: "jacknie8407@gmail.com", password: "pass12", roles: ["ROLE_USER"] as MemberRole[] };

function MemberDetail() {
  const navigate = useNavigate();
  const { memberId } = useParams<Params>();
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
        body: "회원 정보가 수정 되었습니다.",
        onConfirm: ({ close }) => {
          close();
          navigate(-1);
        },
      });
    },
    [publish, navigate],
  );

  return (
    <MemberForm
      member={{ id: memberId, ...member }}
      isPending={isPending}
      submitHandlers={{ onValid }}
      onDelete={() => {
        publish({
          title: "안내",
          body: "회원 정보가 삭제 되었습니다.",
          onConfirm: ({ close }) => {
            close();
            navigate(-1);
          },
        });
      }}
      onCancel={() => navigate(-1)}
    />
  );
}

export default MemberDetail;
