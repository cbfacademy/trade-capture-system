import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
// @ts-expect-error - avatar might not be found
import avatar from '../assets/avatar.svg'
import Button from '../components/Button';
import Input from '../components/Input';
import {authenticate,getUserByLogin} from "../utils/api";
import Snackbar from "../components/Snackbar";
import userStore from "../stores/userStore";
import LoadingSpinner from "../components/LoadingSpinner";
import SignUp from './SignUp';

const SignIn = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const navigate = useNavigate();
    const [isSnackBarOpen, setIsSnackBarOpen] = React.useState(false);
    const [loginError, setLoginError] = React.useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);
    const [isSignUpOpen, setIsSignUpOpen] = useState(false);

    const handleSignIn = async (e: React.FormEvent) => {
        setLoading(true)
        e.preventDefault();
        try {
            const authRes = await authenticate(email, password);
            if (authRes.status === 200) {
                localStorage.setItem("authenticated", "true");
                sessionStorage.setItem("authenticated", "true")
                const userRes = await getUserByLogin(email);
                if (userRes.status === 200) {
                    const user = userRes.data;
                    userStore.user = user;
                    userStore.authorization = user.userProfile;
                    userStore.isLoading = false;
                    console.log("User details fetched successfully:", user);
                    console.log("User profile:", userStore.authorization);
                    setLoading(false)
                    navigate('/home');
                } else {
                    setLoginError("Failed to fetch user details.");
                    setIsSnackBarOpen(true);
                }
            } else {
                setLoginError("Invalid credentials, please try again.");
                setIsSnackBarOpen(true);
            }
        } catch (error) {
            setLoginError("An error occurred during sign in :" + error);
            setIsSnackBarOpen(true);
        } finally {
            setTimeout(() => {
                setIsSnackBarOpen(false)
                setLoginError("");
            }, 3000);
            setLoading(false)
        }
    };

    return (
        <div
            className="relative  w-full min-h-screen flex flex-col items-center justify-center justify-items-center bg-slate-50 ">
            <div className={"mb-10 font-semibold text-2xl"}> Welcome to Trading Platform, please use the below form to
                Login
            </div>
            {loading ? <LoadingSpinner/> :
            <div
                className="rounded-2xl border border-gray-300 shadow-2xl shadow-gray-300 bg-white p-8 gap-y-4 flex flex-col justify-center items-center">
                <form className="flex flex-col gap-4 bg-white p-8 rounded-xl shadow-none" onSubmit={handleSignIn}>
                    <img className="rounded-2xl shadow-lg flex justify-center allign-center w-[200px] h-[200px]"
                         src={avatar} alt="avatar"/>
                    <Input
                        type="userid"
                        name="email"
                        label="Email"
                        required
                        variant="primary"
                        size="md"
                        autoComplete="username"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                    />
                    <Input
                        type="password"
                        name="password"
                        label="Password"
                        required
                        variant="primary"
                        size="md"
                        autoComplete="current-password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />
                    <Button type="submit" variant="primary" size="md" onClick={handleSignIn}>
                        Sign In
                    </Button>
                </form>
                <Button type="button" variant="secondary" size="md" onClick={() => setIsSignUpOpen(true)}>
                    Sign Up
                </Button>
            </div>
            }
                <SignUp isOpen={isSignUpOpen} onClose={() => setIsSignUpOpen(false)} />
                <Snackbar open={isSnackBarOpen} message={loginError} type={loginError !== "" ? 'error' : "success"} onClose={() => setIsSnackBarOpen(false)}/>
        </div>
    );
}

export default SignIn;
