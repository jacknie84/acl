import { PropsWithChildren, useCallback, useMemo, useRef, useState } from "react";
import ConfirmContext from "../contexts/ConfirmContext";
import { ConfirmContextValue, ConfirmModalItem, ConfirmModalRequest } from "../types";
import ConfirmModal from "./ConfirmModal";

function ConfirmProvider({ children }: PropsWithChildren) {
  const [confirm, setConfirm] = useState<ConfirmModalItem>();
  const exitedFlag = useRef(false);
  const publish = useCallback((request: ConfirmModalRequest) => {
    const item = { ...request, isShow: true } as ConfirmModalItem;
    setConfirm(item);
  }, []);
  const onClose = useCallback(() => {
    if (confirm) {
      setConfirm({ ...confirm, isShow: false });
      return new Promise<void>((resolve) => {
        const interval = setInterval(() => {
          if (exitedFlag.current) {
            exitedFlag.current = false;
            resolve();
            clearInterval(interval);
          }
        }, 50);
      });
    } else {
      return Promise.resolve();
    }
  }, [confirm]);
  const onExited = useCallback(() => {
    setConfirm(undefined);
    exitedFlag.current = true;
  }, []);
  const value = useMemo<ConfirmContextValue>(() => ({ publish }), [publish]);

  return (
    <ConfirmContext.Provider value={value}>
      {children}
      {confirm && <ConfirmModal item={confirm} onClose={onClose} onExited={onExited} />}
    </ConfirmContext.Provider>
  );
}

export default ConfirmProvider;
