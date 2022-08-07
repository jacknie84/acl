import { useCallback, useMemo, useState } from "react";
import { Form, ListGroup, OverlayTrigger, Popover } from "react-bootstrap";

type Props = {
  isPending: boolean;
  onSelect: (sid: string, displayName: string) => void;
};

function SidSelect({ isPending, onSelect }: Props) {
  const [isShowOverlay, setShowOverlay] = useState(false);
  const [value, setValue] = useState("");
  const onClickItem = useCallback(
    (sid: string, displayName: string) => {
      onSelect(sid, displayName);
      setValue("");
      setShowOverlay(false);
    },
    [onSelect],
  );
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const onInput = useCallback((value: string) => {
    setValue(value);
    if (value.length > 1) {
      setShowOverlay(true);
    } else {
      setShowOverlay(false);
    }
  }, []);
  const popover = useMemo(
    () => (
      <Popover>
        <Popover.Body>
          <ListGroup variant="flush">
            <ListGroup.Item action onClick={() => onClickItem("1", "사용자1")}>
              사용자1
            </ListGroup.Item>
            <ListGroup.Item action onClick={() => onClickItem("2", "사용자2")}>
              사용자2
            </ListGroup.Item>
            <ListGroup.Item action onClick={() => onClickItem("3", "사용자3")}>
              사용자3
            </ListGroup.Item>
            <ListGroup.Item action onClick={() => onClickItem("4", "사용자4")}>
              사용자4
            </ListGroup.Item>
          </ListGroup>
        </Popover.Body>
      </Popover>
    ),
    [onClickItem],
  );

  return (
    <OverlayTrigger show={isShowOverlay} placement="bottom-start" overlay={popover}>
      <Form.Control
        value={value}
        disabled={isPending}
        autoComplete="off"
        placeholder="권한 부여 대상을 검색해 주세요."
        onInput={(e) => onInput(e.currentTarget.value)}
      />
    </OverlayTrigger>
  );
}

export default SidSelect;
