import React from 'react';
import {useNavigate} from 'react-router-dom';
//@ts-expect-error - Logo might not be found
import logo from '../assets/trading-logo.svg'
import Button from './Button';
import userStore from "../stores/userStore";

const navItems = [
    {label: 'Home', aria: 'home', path: '/home', profile: ['default', 'TRADER_SALES', 'MO', 'SUPPORT', 'ADMIN', 'SUPER_USER']},
    {label: 'Trading', aria: 'trade', path: '/trade' , profile: ['TRADER_SALES', 'MO', 'SUPPORT','SUPER_USER']},
    {label: 'Middle Office', aria: 'trades', path: '/middle-office', profile: ["MO"]},
    {label: 'Support Team', aria: 'support', path: '/support', profile: "SUPPORT"},
    {label: 'Administrator', aria: 'admin', path: '/admin', profile: "ADMIN"},
];

const Navbar = () => {
    const navigate = useNavigate();

    return (
        <>
            <nav className="bg-white w-full shadow-lg mx-auto py-3 flex items-center rounded-2xl">
                <div className={"justify-start gap-6 flex w-1/6"}>
                    <img src={logo} alt={"logo"} className={"ml-2 w-8 h-8"}/>
                    <div className="font-bold font-sans text-lg">Trading Platform</div>
                </div>
                <div className="inline-flex rounded-xl shadow bg-white p-1 gap-2">
                    {navItems.map((item) => (
                        item.profile.includes(userStore.authorization ? userStore.authorization : "") &&
                        <Button
                            key={item.aria}
                            variant="primary"
                            size="md"
                            className="px-4 py-2 rounded-lg !text-black bg-transparent hover:bg-indigo-600 font-semibold transition cursor-pointer shadow-none"
                            aria-label={item.aria}
                            onClick={() => {
                                console.log("Navigating to:", item.path);
                                navigate(item.path);
                            }}
                        >
                            {item.label}
                        </Button>
                    ))}
                </div>
                <div className="flex-1 flex justify-end pr-6">
                    <Button
                        variant="primary"
                        size="sm"
                        className="px-4 py-2 rounded-lg !text-black !bg-gray-200 hover:!bg-gray-500 hover:!text-white font-semibold transition cursor-pointer shadow-none"
                        aria-label="signout"
                        onClick={() => {
                            localStorage.setItem('authenticated', 'false');
                            navigate('/signin');
                        }}
                    >
                        Sign Out
                    </Button>
                </div>
            </nav>
        </>
    );
};

export default Navbar;
