import React from 'react';
import Navbar from './Navbar';
import Sidebar from './Sidebar';

interface LayoutProps {
    children?: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({children}) => (
    <div className="w-full min-h-fit flex flex-col justify-start">
        <Navbar />
        <div className="flex flex-row flex-1 min-h-0">
            <Sidebar />
            <div className="flex-1 flex items-center justify-center  p-1 min-h-0">
                {children}
            </div>
        </div>
    </div>
);

export default Layout;
