import { useMemo, useState } from "react";
import { Button, Modal, Spinner } from "react-bootstrap";
import LoadingButton from "src/components/LoadingButton";
import { ConfirmModalItem } from "../types";

type Props = {
  item: ConfirmModalItem;
  onClose: () => Promise<void>;
  onExited: () => void;
};

function ConfirmModal({ item, onClose, onExited }: Props) {
  const { isShow, title, body, onCancel, onConfirm } = item;
  const [confirmFreeze, setConfirmFreeze] = useState(false);
  const [cancelFreeze, setCancelFreeze] = useState(false);
  const freeze = useMemo(() => confirmFreeze || cancelFreeze, [confirmFreeze, cancelFreeze]);

  return (
    <Modal show={isShow} centered backdrop="static" onExited={onExited}>
      <Modal.Header>{title && <Modal.Title>{title}</Modal.Title>}</Modal.Header>
      <Modal.Body>{body}</Modal.Body>
      <Modal.Footer>
        {onCancel && (
          <Button
            variant="outline-secondary"
            disabled={freeze}
            onClick={() => onCancel({ close: onClose, freeze: setCancelFreeze })}
          >
            취소 {cancelFreeze && <Spinner animation="border" variant="info" size="sm" />}
          </Button>
        )}
        <LoadingButton isLoading={confirmFreeze} onClick={() => onConfirm({ close: onClose, freeze: setConfirmFreeze })}>
          확인
        </LoadingButton>
      </Modal.Footer>
    </Modal>
  );
}

export default ConfirmModal;
