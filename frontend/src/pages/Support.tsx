import React from 'react';
import { useSearchParams } from "react-router-dom";
import { HomeContent } from "../components/HomeContent";
import Layout from "../components/Layout";
import TradeActionsView from "../views/TradeActionsView";

const Support: React.FC = () => {
    const [searchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';
    return (
        <Layout>
            {view === 'default' && <HomeContent/>}
                {view === 'actions' && <TradeActionsView/>}
        </Layout>
    );
};

export default Support;

