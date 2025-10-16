import { useSearchParams } from "react-router-dom";
import { HomeContent } from "../components/HomeContent";
import Layout from "../components/Layout";
import StaticDataActionsView from "../views/StaticDataActionsView";
import TradeActionsView from "../views/TradeActionsView";

const MiddleOffice = () => {
    const [searchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';
    return (
        <Layout>
            {view === 'default' && <HomeContent/>}
            {view === 'actions' && <TradeActionsView/>}
            {view === 'static' && <StaticDataActionsView/>}
        </Layout>
    );
};

export default MiddleOffice;

