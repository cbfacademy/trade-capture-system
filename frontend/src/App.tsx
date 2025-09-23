import {QueryClientProvider} from '@tanstack/react-query';
import AppRouter from "./AppRouter";
import queryClient from './utils/queryClient';

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <div
                id="root"
                className="min-h-screen w-full font-sans text-slate-900 bg-slate-50 bg-[radial-gradient(circle_at_20%_20%,rgba(203,213,225,0.7)_0,transparent_70%),radial-gradient(circle_at_80%_80%,rgba(99,102,241,0.08)_0,transparent_70%)]"
            >
                <AppRouter/>
            </div>
        </QueryClientProvider>
    )
}

export default App
