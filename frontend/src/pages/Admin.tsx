import React from 'react';
import { useSearchParams } from "react-router-dom";
import { HomeContent } from "../components/HomeContent";
import Layout from "../components/Layout";
import AllUserView from "../views/AllUserView";
import UserActionsView from "../views/UserActionsView";

const Admin: React.FC = () => {
    const [searchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';
    return (
        <Layout>
            {view === 'default' && <HomeContent/>}
            {view === 'user-actions' && <UserActionsView/> /* User Admin modal removed from here */}
            {view === 'user-all' && <AllUserView  />}
        </Layout>
    );
};

export default Admin;
