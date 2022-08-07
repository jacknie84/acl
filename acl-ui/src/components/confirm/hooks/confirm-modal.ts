import { useContext } from "react";
import ConfirmContext from "../contexts/ConfirmContext";

function useConfirmModal() {
  const { publish } = useContext(ConfirmContext);
  return publish;
}

export default useConfirmModal;
