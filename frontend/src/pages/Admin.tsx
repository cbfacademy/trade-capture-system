import React from 'react';
import {useSearchParams} from "react-router-dom";
import Layout from "../components/Layout";
import {HomeContent} from "../components/HomeContent";
import {AllUserView} from "../modal/AllUserView";
import {UserActionsModal} from "../modal/UserActionsModal";

const Admin: React.FC = () => {
    const [searchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';
    return (
        <Layout>
            {view === 'default' && <HomeContent/>}
            {view === 'user-actions' && <UserActionsModal/> /* User Admin modal removed from here */}
            {view === 'user-all' && <AllUserView  />}
        </Layout>
    );
};

export default Admin;
