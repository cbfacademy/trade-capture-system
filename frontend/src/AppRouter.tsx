import { observer } from 'mobx-react-lite';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import PrivateRoute from "./components/PrivateRoute";
import Admin from "./pages/Admin";
import Main from './pages/Main';
import MiddleOffice from "./pages/MiddleOffice";
import SignIn from './pages/SignIn';
import Support from "./pages/Support";
import TraderSales from "./pages/TraderSales";
import userStore from './stores/userStore';

const AppRouter = observer(() => (

    <BrowserRouter>
        <Routes>
            <Route path="/signin" element={<SignIn/>}/>
            <Route path="/home" element={<PrivateRoute><Main/></PrivateRoute>}/>
            <Route path="/trade" element={<PrivateRoute><TraderSales/></PrivateRoute>}/>
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