import React, {useState} from 'react';
import {useLocation, useSearchParams} from 'react-router-dom';
import Button from './Button';

const navItems = [
    {label: 'Trade Actions', aria: 'trade-actions', parent: 'trade', param: 'actions'},
    {label: 'History', aria: 'trade-history', parent: 'trade', param: 'history'},
    {label: 'User Actions', aria: 'user-actions', parent: 'admin', param: 'user-actions'},
    {label: 'All Users', aria: 'user-all', parent: 'admin', param: 'user-all'}, // Added for user history
    {label: 'Trade Actions', aria: 'trade-actions', parent: 'middle-office', param: 'actions'},
    {label: 'Static Data Management', aria: 'static-actions', parent: 'middle-office', param: 'static'},
    {label: 'View Trade', aria: 'trade-actions', parent: 'support', param: 'actions'},
];

const Sidebar = () => {
    const [collapsed, setCollapsed] = useState(false);
    const [searchParams, setSearchParams] = useSearchParams();

    const handleSideBarClick = (param: string) => {
        setSearchParams({view: param});
    }
    const location = useLocation();
    const pathSegments = location.pathname.split('/').filter(Boolean);
    const parentRoot = pathSegments[0];
    const filteredItems = navItems.filter(item => item.parent === parentRoot);

    return (
        <aside
            className={`transition-all duration-300 h-screen bg-white shadow-lg mt-1 p-4 flex flex-col rounded-2xl ${collapsed ? 'w-16' : 'w-64'}`}
        >
            <button className="mb-4 p-2 h-fit w-fit rounded hover:bg-gray-200 transition self-end flex"
                    onClick={() => setCollapsed(!collapsed)}
                    aria-label={collapsed ? 'Expand Sidebar' : 'Collapse Sidebar'}>
                <span className="material-icons">
                    {collapsed ? 'chevron_right' : 'chevron_left'}
                </span>
            </button>
            <ul className="space-y-2">
                {filteredItems.map((item) => (
                    <li
                        key={item.aria}
                        className={`${collapsed ? 'hidden' : 'text-xl transition'}`}
                        aria-label={item.aria}
                    >
                        <Button
                            variant="primary"
                            size="md"
                            className="w-full text-left !text-black bg-transparent hover:bg-indigo-300  text-grey shadow-none rounded-lg cursor-pointer"
                            onClick={() => handleSideBarClick(item.param)}
                        >
                            {item.label}
                        </Button>
                    </li>
                ))}
            </ul>
        </aside>
    );
};

export default Sidebar;
