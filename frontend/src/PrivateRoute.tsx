import {Navigate} from "react-router-dom";
import {JSX} from "react";

 const PrivateRoute = ({children}: {children:JSX.Element}) => {
    const  authenticated = localStorage.getItem('authenticated') === 'true' || sessionStorage.getItem("authenticated") === 'true';
    console.log("Authenticated: " + authenticated)
    return authenticated ? children : <Navigate to="/signin" replace /> ;
}
export default PrivateRoute;