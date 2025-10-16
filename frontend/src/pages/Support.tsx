import React from 'react';
import { useSearchParams } from "react-router-dom";
import { HomeContent } from "../components/HomeContent";
import Layout from "../components/Layout";
import TradeActionsModal from "../modal/TradeActionsModal";

const Support: React.FC = () => {
    const [searchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';
    return (
        <Layout>
            {view === 'default' && <HomeContent/>}
            {view === 'actions' && <TradeActionsModal/>}
        </Layout>
    );
};

export default Support;

