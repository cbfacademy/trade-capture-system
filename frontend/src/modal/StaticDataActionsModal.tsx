import React from "react";
import {observer} from "mobx-react-lite";
import Button from "../components/Button";
import LoadingSpinner from "../components/LoadingSpinner";
import Snackbar from "../components/Snackbar";

export const StaticDataActionsModal: React.FC = observer(() => {
    const [loading, setLoading] = React.useState<boolean>(false);
    const [snackbarOpen, setSnackbarOpen] = React.useState<boolean>(false);
    const [snackbarMessage, setSnackbarMessage] = React.useState<string>("");
    const [selectedDataType, setSelectedDataType] = React.useState<string>("");

    const staticDataTypes = [
        "Books",
        "Counterparties",
        "Currencies",
        "Products",
        "User Profiles"
    ];

    const handleRefreshData = async () => {
        setLoading(true);
        try {
            // Simulate API call for refreshing static data
            await new Promise(resolve => setTimeout(resolve, 1000));
            setSnackbarOpen(true);
            setSnackbarMessage(`Successfully refreshed ${selectedDataType || 'all'} static data`);
        } catch (error) {
            setSnackbarOpen(true);
            setSnackbarMessage("Error refreshing static data: " + (error instanceof Error ? error.message : "Unknown error"));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={"flex flex-col rounded-lg drop-shadow-2xl mt-0 bg-indigo-50 w-full h-full p-6"}>
            <div className={"text-center mb-6"}>
                <h2 className={"text-2xl font-bold text-gray-800"}>Static Data Management</h2>
                <p className={"text-gray-600 mt-2"}>Manage and refresh static data used throughout the system</p>
            </div>

            <div className={"flex flex-col gap-4 max-w-md mx-auto"}>
                <div>
                    <label className={"block text-sm font-medium text-gray-700 mb-2"}>
                        Select Data Type (Optional)
                    </label>
                    <select
                        value={selectedDataType}
                        onChange={(e) => setSelectedDataType(e.target.value)}
                        className={"w-full p-2 border border-gray-300 rounded-md bg-white"}
                    >
                        <option value="">All Data Types</option>
                        {staticDataTypes.map((type) => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>
                </div>

                <div className={"flex flex-col gap-2"}>
                    <Button
                        variant={"primary"}
                        type={"button"}
                        size={"md"}
                        onClick={handleRefreshData}
                        className={"w-full"}
                        disabled={loading}
                    >
                        {loading ? "Refreshing..." : "Refresh Static Data"}
                    </Button>
                </div>

                {loading && <LoadingSpinner />}
            </div>

            <div className={"mt-8 text-sm text-gray-500"}>
                <h3 className={"font-semibold mb-2"}>Available Operations:</h3>
                <ul className={"list-disc list-inside space-y-1"}>
                    <li>Refresh static data from external sources</li>
                    <li>Validate data consistency</li>
                    <li>Monitor data freshness</li>
                </ul>
            </div>

            <Snackbar
                open={snackbarOpen}
                message={snackbarMessage}
                onClose={() => setSnackbarOpen(false)}
                type="success"
            />
        </div>
    )
})