import React from "react";
import Input from "../components/Input";
import Button from "../components/Button";
import api from "../utils/api";
import { SingleTradeModal} from "./SingleTradeModal";
import { getDefaultTrade } from "../utils/tradeUtils";
import userStore from "../stores/userStore";
import LoadingSpinner from "../components/LoadingSpinner";
import Snackbar from "../components/Snackbar";
import {observer} from "mobx-react-lite";
import {useQuery} from '@tanstack/react-query';
import staticStore from "../stores/staticStore";
import {Trade, TradeLeg} from "../utils/tradeTypes";

export const TradeActionsModal: React.FC = observer(() => {
    const [tradeId, setTradeId] = React.useState<string>("");
    const [snackBarOpen, setSnackbarOpen] = React.useState<boolean>(false);
    const [trade, setTrade] = React.useState<Trade | null>(null);
    const [loading, setLoading] = React.useState<boolean>(false);
    const [snackbarMessage, setSnackbarMessage] = React.useState<string>("");
    const [isLoadError, setIsLoadError] = React.useState<boolean>(false);
    const [modalKey, setModalKey] = React.useState(0);

    const {isSuccess, error} = useQuery({
        queryKey: ["staticValues"],
        queryFn: () => staticStore.fetchAllStaticValues(),
        refetchInterval: 30000, // 30 seconds in ms
        refetchOnWindowFocus: false, // Optional: disable on tab focus
    });

    React.useEffect(() => {
        if (isSuccess) {
            staticStore.isLoading = false;
            console.log("Static values loaded successfully");
        }
        if (error) {
            staticStore.error = (error).message || 'Unknown error';
        }
    }, [isSuccess, error]);

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Searching for trade ID:", tradeId);
        setLoading(true)
        try {
            const tradeResponse = await api.get(`/trades/${tradeId}`);
            if (tradeResponse.status === 200) {
                // Convert date fields to Date objects
                const convertToDate = (val: string | undefined) => val ? new Date(val) : undefined;
                const tradeData = tradeResponse.data;
                const dateFields = [
                    'tradeDate',
                    'startDate',
                    'maturityDate',
                    'executionDate',
                    'lastTouchTimestamp',
                    'validityStartDate'
                ];
                // Helper to format Date to yyyy-mm-dd
                const formatDateForInput = (date: Date | undefined) =>
                    date ? date.toISOString().slice(0, 10) : undefined;
                dateFields.forEach(field => {
                    if (tradeData[field]) {
                        const dateObj = convertToDate(tradeData[field]);
                        tradeData[field] = formatDateForInput(dateObj);
                    }
                });
                if (Array.isArray(tradeData.tradeLegs)) {
                    console.log(`Found ${tradeData.tradeLegs.length} trade legs in the response`);
                    tradeData.tradeLegs = tradeData.tradeLegs.map((leg: TradeLeg) => {
                        // If any date fields exist in legs, convert here as well
                        // Ensure leg data is properly structured for the UI
                        console.log("Processing leg:", leg);
                        return {
                            ...leg,
                            // Ensure these properties exist even if null for the UI
                            legId: leg.legId || '',
                            legType: leg.legType || '',
                            rate: leg.rate !== undefined ? leg.rate : '',
                            index: leg.index || '',
                        };
                    });
                } else {
                    console.warn("No trade legs found in the response!");
                    // Initialize with empty array to prevent null reference errors
                    tradeData.tradeLegs = [];
                }
                setTrade(tradeData);
                setSnackbarOpen(true)
                setSnackbarMessage("Successfully fetched trade details");

            } else {
                console.error("Error fetching trade:", tradeResponse.statusText);
                setSnackbarMessage("Error fetching trade details: " + tradeResponse.statusText);
                setIsLoadError(true)
            }
        } catch (error) {
            console.error("Error fetching trade:", error);
            setIsLoadError(true);
            setSnackbarOpen(true);
            setSnackbarMessage("Error fetching trade details: " + (error instanceof Error ? error.message : "Unknown error"));
        } finally {
            setTimeout(() => {
                setSnackbarOpen(false);
                setSnackbarMessage("")
                setIsLoadError(false)
            }, 3000);
            setLoading(false)
            setTradeId("")
        }
    };
    // Add a handler to fully clear all trade state and cache
    const handleClearAll = () => {
        setTrade(null);
        setTradeId("");
        setSnackbarOpen(false);
        setSnackbarMessage("");
        setIsLoadError(false);
        setLoading(false);
    };
    const handleBookNew = () => {
        const defaultTrade = getDefaultTrade();
        console.log('DEBUG getDefaultTrade:', defaultTrade);
        setTrade(defaultTrade);
        setModalKey(prev => prev + 1);
    };
    const mode = userStore.authorization === "TRADER_SALES" || userStore.authorization === "MO" ? "edit" : "view";
    return (
        <div className={"flex flex-col rounded-lg drop-shadow-2xl mt-0 bg-indigo-50 w-full h-full"}>
            <div className={"flex flex-row items-center justify-center p-4 h-fit w-fit gap-x-2 mb-2 mx-auto"}>
                <Input size={"sm"}
                       type={"search"}
                       required
                       placeholder={"Search by Trade ID"}
                       key={"trade-id"}
                       value={tradeId}
                       onChange={(e) => setTradeId(e.currentTarget.value)}
                       className={"bg-white h-fit w-fit"}/>
                <Button variant={"primary"} type={"button"} size={"sm"} onClick={handleSearch}
                        className={"w-fit h-fit"}>Search</Button>
                <Button variant={"primary"} type={"button"} size={"sm"} onClick={handleClearAll}
                        className={"w-fit h-fit !bg-gray-500 hover:!bg-gray-700"}>Clear</Button>
                { userStore.authorization === "TRADER_SALES" &&
                <Button variant={"primary"} type={"button"} size={"sm"} onClick={handleBookNew}
                        className={"w-fit h-fit"}>Book New</Button>
                }
            </div>
            <div>
                {loading ? <LoadingSpinner/> : null}
                {trade && !loading && <SingleTradeModal key={modalKey} mode={mode} trade={trade} isOpen={!!trade} onClear={handleClearAll}/>}
            </div>
            <Snackbar open={snackBarOpen} message={snackbarMessage} onClose={() => setSnackbarOpen(false)}
                      type={isLoadError ? "error" : "success"}/>
        </div>
    )
})