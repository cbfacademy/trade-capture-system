import React from "react";
import Button from "../components/Button";
import {Trade, TradeLeg} from "../utils/tradeTypes";
import CashflowModal from "./CashflowModal";
import api from "../utils/api";
import {CashflowDTO} from "../utils/tradeTypes";
import Snackbar from "../components/Snackbar";
import TradeDetails from "../components/TradeDetails";
import TradeLegDetails from "../components/TradeLegDetails";
import {getDefaultTrade, validateTrade, formatTradeForBackend, convertEmptyStringsToNull} from "../utils/tradeUtils";
import {formatDatesFromBackend} from "../utils/dateUtils";
import LoadingSpinner from "../components/LoadingSpinner";
import userStore from "../stores/userStore";

/**
 * Props for SingleTradeModal component
 */
interface SingleTradeModalProps {
    mode: "view" | "edit";
    trade?: Trade;
    isOpen: boolean;
    onClear?: () => void;
}

/**
 * Modal component for viewing, editing and managing a single trade
 */
export const SingleTradeModal: React.FC<SingleTradeModalProps> = (props) => {
    const [editableTrade, setEditableTrade] = React.useState<Trade | undefined>(props.trade ?? getDefaultTrade());
    const [cashflowModalOpen, setCashflowModalOpen] = React.useState(false);
    const [generatedCashflows, setGeneratedCashflows] = React.useState<CashflowDTO[]>([]);
    const [snackbarOpen, setSnackbarOpen] = React.useState(false);
    const [snackbarMsg, setSnackbarMsg] = React.useState("");
    const [snackbarType, setSnackbarType] = React.useState<'success' | 'error'>("success");
    const [loading, setLoading] = React.useState(false);

    React.useEffect(() => {
        setEditableTrade(props.trade ?? getDefaultTrade());
        setCashflowModalOpen(false);
        setGeneratedCashflows([]);
        setSnackbarOpen(false);
        setSnackbarMsg("");
        setSnackbarType("success");
    }, [props.trade, props.isOpen]);

    /**
     * Handles field changes in the trade header
     */
    const handleFieldChange = (key: keyof Trade, value: unknown) => {
        setEditableTrade((prev) => prev ? ({
            ...prev,
            [key]: value,
        }) : prev);
    };

    /**
     * Generates cashflows for the current trade
     */
    const generateCashflows = async () => {
        setLoading(true)
        if (!editableTrade) return;
        try {
            // Map each leg to the backend's expected DTO structure
            const legsDto = editableTrade.tradeLegs.map(leg => ({
                legType: leg.legType,
                notional: typeof leg.notional === 'string' ? parseFloat(leg.notional) : leg.notional,
                rate: leg.rate ? (typeof leg.rate === 'string' ? parseFloat(leg.rate) : leg.rate) : undefined,
                index: leg.index,
                calculationPeriodSchedule: leg.calculationPeriodSchedule,
                paymentBusinessDayConvention: leg.paymentBusinessDayConvention,
                payReceiveFlag: leg.payReceiveFlag,
            }));

            const response = await api.post("/cashflows/generate", {
                legs: legsDto,
                tradeStartDate: editableTrade.startDate,
                tradeMaturityDate: editableTrade.maturityDate,
            });

            const allCashflows: CashflowDTO[] = response.data;

            // Assign cashflows to respective legs by payRec (Pay/Rec) and legType
            const updatedLegs = editableTrade.tradeLegs.map(leg => ({
                ...leg,
                cashflows: allCashflows.filter(cf =>
                    cf.payRec?.toLowerCase() === (leg.payReceiveFlag || '').toLowerCase() &&
                    cf.paymentType?.toLowerCase() === (leg.legType || '').toLowerCase()
                )
            }));

            setEditableTrade(trade => trade ? ({...trade, tradeLegs: updatedLegs}) : trade);
            setGeneratedCashflows(allCashflows);
            setCashflowModalOpen(true);

        } catch {
            setSnackbarMsg('Failed to generate cashflows');
            setSnackbarType('error');
            setSnackbarOpen(true);
        } finally {
            setLoading(false)
        }
    };

    /**
     * Handles field changes in trade legs
     */
    const handleLegFieldChange = (legIdx: number, key: keyof TradeLeg, value: unknown) => {
        setEditableTrade((prev) => {
            if (!prev) return prev;
            // Always update the full array, not a slice
            const updatedLegs = prev.tradeLegs.map((leg, idx) =>
                idx === legIdx ? {...leg, [key]: value} : leg
            );
            return {
                ...prev,
                tradeLegs: updatedLegs,
            };
        });
    };

    /**
     * Handles saving the trade
     */
    const handleSaveTrade = async () => {
        setLoading(true)
        if (!editableTrade) return;
        const validationError = validateTrade(editableTrade);
        if (validationError) {
            setSnackbarMsg(validationError);
            setSnackbarType('error');
            setSnackbarOpen(true);
            setTimeout(() => setSnackbarOpen(false), 3000);
            return;
        }

        let tradeDto = formatTradeForBackend(editableTrade);
        // Convert empty strings for numeric/date fields to null before sending to backend
        tradeDto = convertEmptyStringsToNull(tradeDto);

        try {
            if (editableTrade.tradeId) {
                // Update existing trade
                await api.put(`/trades/${editableTrade.tradeId}`, tradeDto);
                setSnackbarMsg(`Trade updated successfully! Trade ID: ${editableTrade.tradeId}`);
                setSnackbarType('success');
                setSnackbarOpen(true);

                // Fetch the updated trade to ensure the view shows the most recent data
                const response = await api.get(`/trades/${editableTrade.tradeId}`);
                const updatedTrade = formatDatesFromBackend(response.data);
                setEditableTrade(updatedTrade);
            } else {
                // Save new trade
                const response = await api.post('/trades', tradeDto);
                const newTradeId = response.data?.tradeId || response.data?.id || '';
                setSnackbarMsg(`Trade saved successfully! Trade ID: ${newTradeId}`);
                setSnackbarType('success');
                setSnackbarOpen(true);
            }
        } catch (e) {
            setSnackbarMsg('Failed to save trade: ' + (e instanceof Error ? e.message : 'Unknown error'));
            setSnackbarType('error');
            setSnackbarOpen(true);
        } finally {
            setLoading(false)
            setTimeout(() => {
                setSnackbarOpen(false);
                setSnackbarMsg("");
                setSnackbarType("success");
            }, 3000);
        }
    };

    /**
     * Handles terminating the trade
     */
    const handleTerminateTrade = async () => {
        setLoading(true)
        // Check if trade ID exists and validate if the trade is in a terminable state
        if (!editableTrade?.tradeId) {
            setSnackbarMsg('Cannot terminate: Trade ID is missing');
            setSnackbarType('error');
            setSnackbarOpen(true);
            return;
        }

        // Prevent terminating already terminated trades
        if (editableTrade.tradeStatus === 'TERMINATED') {
            setSnackbarMsg('This trade has already been terminated');
            setSnackbarType('error');
            setSnackbarOpen(true);
            return;
        }

        try {
            // Call the terminate endpoint
            await api.post(`/trades/${editableTrade.tradeId}/terminate`);

            // Show success message
            setSnackbarMsg(`Trade ${editableTrade.tradeId} terminated successfully!`);
            setSnackbarType('success');
            setSnackbarOpen(true);

            // Update the trade status locally to show the terminated state
            setEditableTrade(prev => prev ? {
                ...prev,
                tradeStatus: 'TERMINATED'
            } : prev);

        } catch (error) {
            // Show error message with details
            const errorMessage = error instanceof Error
                ? error.message
                : 'An unknown error occurred while terminating the trade';

            setSnackbarMsg(`Failed to terminate trade: ${errorMessage}`);
            setSnackbarType('error');
            setSnackbarOpen(true);
        } finally {
            // Reset snackbar state after a timeout
            setTimeout(() => {
                setSnackbarOpen(false);
                setSnackbarMsg("");
                setSnackbarType("success");
            }, 3000);
            setLoading(false)
        }
    };

    // Assign 2 trade legs from the trade (if available)
    // Use the full array for updates, only slice for rendering
    const tradeLegs = editableTrade?.tradeLegs ? editableTrade.tradeLegs.slice(0, 2) : [];

    return (
        <div className={"flex flex-col"}>
            <div className={"flex flex-row justify-center w-full"}>
                <h2 className={"text-2xl font-semibold justify-center"}>
                    {props.mode === "edit" ? "Edit Trade" : "View Trade"}
                </h2>
            </div>
            {loading ? <LoadingSpinner/> :
                <div className={"flex flex-row"}>

                    <div key={"Trade Header"} className="flex flex-col ml-2">
                        <h3 className="text-lg font-semibold text-center mb-2">Trade Header</h3>
                        <TradeDetails
                            trade={editableTrade}
                            mode={props.mode}
                            onFieldChange={props.mode === 'edit' ? handleFieldChange : undefined}
                        />
                    </div>
                    {tradeLegs.length > 0 && (
                        <div className="flex flex-row gap-x-8 h-fit justify-center mt-0">
                            {tradeLegs.map((leg, idx) => (
                                <div key={leg.legId || idx + '-' + leg.payReceiveFlag} className="flex flex-col ml-2">
                                    <h3 className="text-lg font-semibold text-center mb-2">Leg {idx + 1}</h3>
                                    <TradeLegDetails
                                        leg={leg}
                                        mode={props.mode}
                                        onFieldChange={props.mode === 'edit' ? (key, value) => handleLegFieldChange(idx, key, value) : undefined}
                                    />
                                    {props.mode === 'edit' && idx === tradeLegs.length - 1 && (
                                        <div className="mt-4 flex justify-end gap-x-2">
                                            <Button
                                                variant={"primary"}
                                                type={"button"}
                                                size={"sm"}
                                                disabled={generatedCashflows.length === 0}
                                                className={generatedCashflows.length === 0 ? 'opacity-50 cursor-not-allowed' : ''}
                                                onClick={handleSaveTrade}
                                                title={generatedCashflows.length === 0 ? "Please generate cashflows first before saving the trade" : ""}
                                            >
                                                Save Trade
                                            </Button>
                                            <Button
                                                variant={"primary"}
                                                type={"button"}
                                                size={"sm"}
                                                onClick={generateCashflows}
                                                className={"!bg-amber-400 hover:!bg-amber-600"}
                                            >
                                                Cashflows
                                            </Button>
                                            {userStore.authorization === "TRADER_SALES" &&
                                            <Button
                                                variant={"secondary"}
                                                type={"button"}
                                                size={"sm"}
                                                onClick={handleTerminateTrade}
                                            >
                                                Terminate Trade
                                            </Button>
                                            }
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            }
            {cashflowModalOpen && (
                <CashflowModal isOpen={cashflowModalOpen} onClose={() => setCashflowModalOpen(false)}
                               cashflows={generatedCashflows}/>
            )}
            <Snackbar open={snackbarOpen} message={snackbarMsg} type={snackbarType}
                      onClose={() => setSnackbarOpen(false)}/>
        </div>
    );
};

export default SingleTradeModal;
