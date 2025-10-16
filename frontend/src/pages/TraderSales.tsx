import { useSearchParams } from "react-router-dom";
import { HomeContent } from "../components/HomeContent";
import Layout from "../components/Layout";
import TradeActionsView from "../views/TradeActionsView";
import TradeBlotterView from "../views/TradeBlotterView";

const TraderSales = () => {
    const [searchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';

    return (
        <div>
            <Layout>
                {view === 'default' && <HomeContent/>}
                {view === 'actions' && <TradeActionsView/>}
                {view === 'history' && <TradeBlotterView/>}
            </Layout>
        </div>
    );
};

export default TraderSales;
