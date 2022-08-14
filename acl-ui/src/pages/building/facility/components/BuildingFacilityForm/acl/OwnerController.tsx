import { useCallback, useEffect, useMemo, useState } from "react";
import { Form, ListGroup, OverlayTrigger, Popover } from "react-bootstrap";
import {
  Control,
  Controller,
  ControllerFieldState,
  ControllerRenderProps,
  UseFormRegister,
  UseFormStateReturn,
} from "react-hook-form";
import { SaveBuildingFacility } from "src/hooks/api/building-facility";

type Props = { isPending: boolean; control: Control<SaveBuildingFacility>; register: UseFormRegister<SaveBuildingFacility> };

function OwnerController({ isPending, control, register }: Props) {
  useEffect(() => {
    register("acl.ownerMemberId", { required: "시설 소유자를 선택해 주세요." });
  }, [register]);

  return (
    <Controller
      name="acl.ownerMemberId"
      control={control}
      render={(props) => {
        const {
          fieldState: { error },
        } = props;

        return (
          <Form.Group className="mt-3" controlId="owner">
            <Form.Label>시설 소유자</Form.Label>
            <OwnerSelect isPending={isPending} {...props} />
            {error && <Form.Control.Feedback type="invalid">{error.message}</Form.Control.Feedback>}
          </Form.Group>
        );
      }}
    />
  );
}

type OwnerSelectProps = {
  isPending: boolean;
  field: ControllerRenderProps<SaveBuildingFacility, "acl.ownerMemberId">;
  fieldState: ControllerFieldState;
  formState: UseFormStateReturn<SaveBuildingFacility>;
};

function OwnerSelect({ isPending, field: { onChange }, fieldState: { error }, formState: { isSubmitted } }: OwnerSelectProps) {
  const [isShowOverlay, setShowOverlay] = useState(false);
  const [name, setName] = useState("");
  const onSelect = useCallback(
    (memberId: number | string, name: string) => {
      onChange(memberId);
      setName(name);
      setShowOverlay(false);
    },
    [onChange],
  );
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const onInput = useCallback((value: string) => {
    setName(value);
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
            <ListGroup.Item action onClick={() => onSelect(1, "사용자1")}>
              사용자1
            </ListGroup.Item>
            <ListGroup.Item action onClick={() => onSelect(2, "사용자2")}>
              사용자2
            </ListGroup.Item>
            <ListGroup.Item action onClick={() => onSelect(3, "사용자3")}>
              사용자3
            </ListGroup.Item>
            <ListGroup.Item action onClick={() => onSelect(4, "사용자4")}>
              사용자4
            </ListGroup.Item>
          </ListGroup>
        </Popover.Body>
      </Popover>
    ),
    [onSelect],
  );

  return (
    <OverlayTrigger show={isShowOverlay} placement="bottom-start" overlay={popover}>
      <Form.Control
        value={name}
        disabled={isPending}
        autoComplete="off"
        placeholder="Enter owner name"
        onInput={(e) => onInput(e.currentTarget.value)}
        isValid={isSubmitted && !Boolean(error)}
        isInvalid={isSubmitted && Boolean(error)}
      />
    </OverlayTrigger>
  );
}

export default OwnerController;
