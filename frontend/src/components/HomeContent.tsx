import React  from "react";

export const HomeContent:React.FC = () => {
    return (
        <div className="flex flex-col items-center justify-center w-full h-full">
            <h1 className="text-3xl font-bold mb-4">Welcome to the Trade Platform</h1>
            <p className="text-lg text-gray-600">Use the navigation to access trades, entities, and admin features.</p>
        </div>
    );
}