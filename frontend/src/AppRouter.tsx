import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import {observer} from 'mobx-react-lite';
import SignIn from './pages/SignIn';
import Main from './pages/Main';
import userStore from './stores/userStore';
import TraderSales from "./pages/TraderSales";
import MiddleOffice from "./pages/MiddleOffice";
import Support from "./pages/Support";
import Admin from "./pages/Admin";
import PrivateRoute from "./PrivateRoute";

const AppRouter = observer(() => (

    <BrowserRouter>
        <Routes>
            <Route path="/signin" element={<SignIn/>}/>
            <Route path="/home" element={<PrivateRoute><Main/></PrivateRoute>}/>
            <Route path="/trade" element={<PrivateRoute> <TraderSales/> </PrivateRoute>}/>
            <Route path="/middle-office" element={<PrivateRoute><MiddleOffice/></PrivateRoute>}/>
            <Route path="/support" element={<PrivateRoute><Support/></PrivateRoute>}/>
            <Route path="/admin" element={<PrivateRoute><Admin/></PrivateRoute>}/>
            <Route
                path="*"
                element={
                    userStore.user ? <Navigate to="/home" replace/> : <Navigate to="/signin" replace/>
                }
            />
        </Routes>
    </BrowserRouter>
));

export default AppRouter;