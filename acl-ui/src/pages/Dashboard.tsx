import { Card, Col, Row } from "react-bootstrap";
import { generatePath, Link } from "react-router-dom";

const buildingTwins = [
  [
    { id: 1, name: "잠실 꼬마 빌딩" },
    { id: 2, name: "강남 꼬마 빌딩" },
  ],
  [
    { id: 3, name: "강북 꼬마 빌딩" },
    { id: 4, name: "분당 꼬마 빌딩" },
  ],
  [{ id: 5, name: "수원 꼬마 빌딩" }],
];

function genPath(id: number) {
  return generatePath("/buildings/:id/tabs/facility", { id: `${id}` });
}

function Home() {
  return (
    <>
      {buildingTwins.map(([first, second], index) => (
        <Row key={index} className="mb-2">
          <Col>
            <Card body>
              <Link to={genPath(first.id)}>{first.name}</Link>
            </Card>
          </Col>
          <Col>
            {second && (
              <Card body>
                <Link to={genPath(second.id)}>{second.name}</Link>
              </Card>
            )}
          </Col>
        </Row>
      ))}
    </>
  );
}

export default Home;
