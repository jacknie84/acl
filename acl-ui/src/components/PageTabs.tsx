import { Tab, TabPaneProps, Tabs } from "react-bootstrap";
import { Outlet } from "react-router-dom";

type Props = { activeTabKey: string; variant?: "tabs" | "pills"; tabs: TabPaneProps[]; onSelectTab: (eventKey: string) => void };

function PageTabs({ activeTabKey, variant, tabs, onSelectTab }: Props) {
  return (
    <Tabs className="mt-3" activeKey={activeTabKey} onSelect={(key) => key && onSelectTab(key)} variant={variant}>
      {tabs.map((tab) => (
        <Tab key={tab.eventKey} {...tab} title={tab.title}>
          <Outlet />
        </Tab>
      ))}
    </Tabs>
  );
}

export default PageTabs;
