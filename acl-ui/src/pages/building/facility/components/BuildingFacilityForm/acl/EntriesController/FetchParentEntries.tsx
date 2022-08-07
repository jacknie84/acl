import { useCallback } from "react";
import { TbBookDownload } from "react-icons/tb";
import { AclEntry } from "src/pages/building/facility/types";
import { waitingAsync } from "src/utils/promise";

type Props = { isPending: boolean; onFetching: (isFetching: boolean) => void; onFetched: (entries: AclEntry[]) => void };

function FetchParentEntries({ isPending, onFetching, onFetched }: Props) {
  const onClickIcon = useCallback(async () => {
    if (isPending) {
      return;
    }
    onFetching(true);
    await waitingAsync(1000);
    onFetching(false);
    onFetched([{ sid: "ROLE_ADMIN", label: "총 관리자", admin: true, create: true, read: true, remove: true, write: true }]);
  }, [isPending, onFetching, onFetched]);

  return <TbBookDownload onClick={onClickIcon} />;
}

export default FetchParentEntries;
