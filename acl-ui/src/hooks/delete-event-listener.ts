import { useCallback } from "react";
import { useConfirmModal } from "src/components/confirm";

function useDeleteEventListener(
  deleteAsync: () => Promise<void>,
  onFulfilled?: () => void,
  onRejected?: (error: any, close: () => Promise<void>) => void,
) {
  const publish = useConfirmModal();

  return useCallback(() => {
    publish({
      title: "경고",
      body: "정말 삭제 하시겠습니까?",
      onConfirm: async ({ freeze, close }) => {
        freeze(true);
        try {
          await deleteAsync();
          await close();
          onFulfilled && onFulfilled();
        } catch (error: any) {
          onRejected && onRejected(error, close);
        } finally {
          freeze(false);
        }
      },
      onCancel: ({ close }) => close(),
    });
  }, [deleteAsync, onFulfilled, onRejected, publish]);
}

export default useDeleteEventListener;
