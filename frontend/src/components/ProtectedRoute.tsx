import React, {ReactNode} from 'react';
import {Navigate} from 'react-router-dom';

interface ProtectedRouteProps {
    isAllowed: boolean;
    redirectPath: string;
    children: ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({isAllowed, redirectPath = '/signin', children}) => {
    if (!isAllowed) {
        return <Navigate to={redirectPath} replace/>;
    }
    return children;
};

export default ProtectedRoute;

