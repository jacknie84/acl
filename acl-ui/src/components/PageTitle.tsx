type Props = { text: string };

function PageTitle({ text }: Props) {
  return <h1 className="bd-title">{text}</h1>;
}

export default PageTitle;
