import { ReactNode } from "react";

export type ConfirmContextValue = { publish: (request: ConfirmModalRequest) => void };
export type ButtonContext = {
  freeze: (flag: boolean) => void;
  close: () => Promise<void>;
};
export type ConfirmModalItem = {
  isShow: boolean;
  title?: ReactNode;
  body: ReactNode;
  onCancel?: (context: ButtonContext) => void;
  onConfirm: (context: ButtonContext) => void;
};

export type ConfirmModalRequest = Omit<ConfirmModalItem, "isShow">;
