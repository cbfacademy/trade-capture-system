import React from "react";
import Label from "./Label";
import FieldRenderer from "./FieldRenderer";
import { Trade } from "../utils/tradeTypes";
import { TRADE_FIELDS, DISABLED_FIELDS } from "../utils/tradeFormFields";

/**
 * Props for the TradeDetails component
 */
export interface TradeDetailsProps {
    trade?: Trade;
    mode: "view" | "edit";
    onFieldChange?: (key: keyof Trade, value: unknown) => void;
};

/**
 * Displays and handles trade header details form
 */
const TradeDetails: React.FC<TradeDetailsProps> = ({trade, mode, onFieldChange}) => {
    if (!trade) return null;
    const labelClass = `h-9 flex items-center font-semibold rounded shadow bg-violet-50 px-2 py-1 ml-1 min-w-[200px] text-sm`;

    return (
        <div className="mt-4 grid p-0 bg-violet-100 rounded shadow text-sm w-fit ml-1">
            {TRADE_FIELDS.map(field => (
                <div className="flex flex-row gap-x-10 items-center mb-1" key={field.key}>
                    <Label className={labelClass} htmlFor={field.key}>
                        {field.label}
                    </Label>
                    <div className="flex flex-row justify-end rounded px-2 py-1 h-fit w-full">
                        <FieldRenderer
                            field={field}
                            value={trade[field.key]}
                            disabled={mode !== 'edit' || DISABLED_FIELDS.includes(field.key)}
                            onChange={(value) => onFieldChange && onFieldChange(field.key, value)}
                        />
                    </div>
                </div>
            ))}
        </div>
    );
};

export default TradeDetails;
