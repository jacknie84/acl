import { Outlet } from "react-router-dom";

type Props = { title: string };

function PageView({ title }: Props) {
  return (
    <>
      <h1 className="bd-title">{title}</h1>
      <Outlet />
    </>
  );
}

export default PageView;
