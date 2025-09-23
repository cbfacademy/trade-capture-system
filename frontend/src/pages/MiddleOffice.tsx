import React from 'react';
import {useSearchParams} from "react-router-dom";
import Layout from "../components/Layout";
import {HomeContent} from "../components/HomeContent";
import {TradeActionsModal} from "../modal/TradeActionsModal";
import {StaticDataActionsModal} from "../modal/StaticDataActionsModal";

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

