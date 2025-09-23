import React from "react";
import Label from "./Label";
import FieldRenderer from "./FieldRenderer";
import { TradeLeg } from "../utils/tradeTypes";
import { TRADE_LEG_FIELDS, DISABLED_LEG_FIELDS, FLOATING_FIELDS, FIXED_FIELDS } from "../utils/tradeFormFields";

/**
 * Props for the TradeLegDetails component
 */
export interface TradeLegDetailsProps {
    leg: TradeLeg;
    mode: "view" | "edit";
    onFieldChange?: (key: keyof TradeLeg, value: unknown) => void;
};

/**
 * Displays and handles trade leg details form
 */
const TradeLegDetails: React.FC<TradeLegDetailsProps> = ({leg, mode, onFieldChange}) => {
    if (!leg) return null;
    const labelClass = `h-7 flex items-center font-semibold rounded shadow bg-violet-50 px-2 py-1 ml-1 min-w-[200px] text-sm`;

    return (
        <div className="mt-4 grid p-0 bg-indigo-100 rounded shadow text-sm w-fit ml-1">
            {TRADE_LEG_FIELDS.map(field => {
                let isDisabled = mode !== 'edit' || DISABLED_LEG_FIELDS.includes(field.key);
                if (leg.legType === 'Fixed' && FLOATING_FIELDS.includes(field.key)) {
                    isDisabled = true;
                }
                if (leg.legType === 'Floating' && FIXED_FIELDS.includes(field.key)) {
                    isDisabled = true;
                }

                return (
                    <div className="flex flex-row gap-x-10 items-center mb-1" key={field.key}>
                        <Label className={labelClass} htmlFor={field.key}>
                            {field.label}
                        </Label>
                        <div className="flex flex-row justify-end rounded px-2 py-1 h-fit w-full">
                            <FieldRenderer
                                field={field}
                                value={leg[field.key]}
                                disabled={isDisabled}
                                onChange={(e: React.ChangeEvent<HTMLInputElement> | string) =>
                                    onFieldChange && onFieldChange(
                                        field.key,
                                        (typeof e === 'string' ? e : e.target ? e.target.value : e)
                                    )
                                }
                            />
                        </div>
                    </div>
                );
            })}
        </div>
    );
};

export default TradeLegDetails;
