import React from 'react';
import { useSearchParams } from "react-router-dom";
import { HomeContent } from "../components/HomeContent";
import Layout from "../components/Layout";
import StaticDataActionsModal from "../modal/StaticDataActionsModal";
import TradeActionsModal from "../modal/TradeActionsModal";

const MiddleOffice = () => {
    const [searchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';
    return (
        <Layout>
            {view === 'default' && <HomeContent/>}
            {view === 'actions' && <TradeActionsModal/>}
            {view === 'static' && <StaticDataActionsModal/>}
        </Layout>
    );
};

export default MiddleOffice;

