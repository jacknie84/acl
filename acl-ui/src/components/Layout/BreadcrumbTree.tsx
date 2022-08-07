import { useCallback, useMemo } from "react";
import { Breadcrumb } from "react-bootstrap";
import { matchPath, useLocation, useMatch, useNavigate } from "react-router-dom";
import { DomainTree } from "./types";

type BreadcrumbData = { name: string; path?: string; active: boolean };

type BreadcrumbTreeProps = {
  domainTree: DomainTree;
};

function BreadcrumbTree({ domainTree }: BreadcrumbTreeProps) {
  const navigate = useNavigate();
  const match = useMatch(domainTree.pattern);
  const traverse = useTraverse();
  const breadcrumb = useMemo(() => {
    if (!match) {
      return [];
    }
    const { path, name, children = [] } = domainTree;
    const data = [] as BreadcrumbData[];
    const active = traverse(children, data);
    return [{ path, name, active }, ...data];
  }, [domainTree, match, traverse]);

  return (
    <>
      {breadcrumb.map(({ path, name, active }, index) =>
        active ? (
          <Breadcrumb.Item key={index} active>
            {name}
          </Breadcrumb.Item>
        ) : (
          <Breadcrumb.Item key={index} onClick={() => path && navigate(path)}>
            {name}
          </Breadcrumb.Item>
        ),
      )}
    </>
  );
}

function useTraverse() {
  const location = useLocation();

  const traverse = useCallback(
    (children: DomainTree[], data: BreadcrumbData[]): boolean => {
      const child = children.find((child) => Boolean(matchPath(child.pattern, location.pathname)));
      if (child) {
        const { path, name, children = [] } = child;
        const active = traverse(children, data);
        data.unshift({ path, name, active });
        if (data.length > 0) {
          return false;
        }
      }
      return true;
    },
    [location],
  );

  return traverse;
}

export default BreadcrumbTree;
