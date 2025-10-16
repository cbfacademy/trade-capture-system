import { observer } from "mobx-react-lite";
import React, { useState } from 'react';
import { useSearchParams } from "react-router-dom";
import { HomeContent } from "../components/HomeContent";
import Layout from '../components/Layout';
import { ApplicationUser } from "../types/user";
import AllUserView from '../views/AllUserView';
import UserActionsView from '../views/UserActionsView';

const Main: React.FC = observer(() => {
    const [searchParams, setSearchParams] = useSearchParams();
    const view = searchParams.get('view') || 'default';
    const [selectedUser, setSelectedUser] = useState<ApplicationUser| null>(null);

    const handleSetView = (newView: string, user?: ApplicationUser) => {
        setSearchParams({ view: newView });
        if (user) setSelectedUser(user);
    };

    return (
        <Layout>
            {view === 'default' && <HomeContent/>}
            {view === 'all-users' && (
                <AllUserView />
            )}
        {view === 'user-actions' && (
            <UserActionsView user={selectedUser || undefined} setView={handleSetView} />
        )}
        </Layout>
    );
});

export default Main;
