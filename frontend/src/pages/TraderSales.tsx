import { useSearchParams } from "react-router-dom";
import { HomeContent } from "../components/HomeContent";
import Layout from "../components/Layout";
import TradeActionsModal from "../modal/TradeActionsModal";
import TradeBlotterModal from "../modal/TradeBlotterModal";

const TraderSales = () => {
    const [searchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';

    return (
        <div>
            <Layout>
                {view === 'default' && <HomeContent/>}
                {view === 'actions' && <TradeActionsModal/>}
                {view === 'history' && <TradeBlotterModal/>}
            </Layout>
        </div>
    );
};

export default TraderSales;
