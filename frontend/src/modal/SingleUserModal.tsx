import { observer } from "mobx-react-lite";
import React from "react";
import Button from "../components/Button";
import Dropdown from "../components/Dropdown";
import Input from "../components/Input";
import Label from "../components/Label";
import staticStore from "../stores/staticStore";
import { ApplicationUser } from "../utils/ApplicationUser";

interface UserDetailModalProps {
    isOpen: boolean;
    onClose: () => void;
    onClickSave: () => void;
    user: ApplicationUser | null;
    setUser: React.Dispatch<React.SetStateAction<ApplicationUser | null>>;
}

const USER_FIELDS = [
    {key: "firstName", label: "First Name", type: "input"},
    {key: "lastName", label: "Last Name", type: "input"},
    {key: "loginId", label: "User Id", type: "input"},
    {key: "password", label: "Password", type: "input", hideIf: (user: ApplicationUser) => !!user.id},
    {key: "active", label: "Active", type: "checkbox"},
    {
        key: "userProfile",
        label: "User Profile",
        type: "dropdown",
        options: () => (staticStore.userTypeCache ?? []).map(u => ({value: u.userType, label: u.userType}))
    },
];

const SingleUserModal: React.FC<UserDetailModalProps> = observer((props) => {
    const {user, setUser} = props;
    if (!user) return null;

    React.useEffect(() => {
        if (staticStore.userTypeCache.length === 0) {
            staticStore.fetchUserProfiles();
        }
    }, []);


    const handleFieldChange = (field: keyof ApplicationUser, value: unknown) => {
        setUser(u => u ? {...u, [field]: value} : u);
    };


    return (
        <div className="bg-violet-50 mt-10 w-full max-w-xl mx-auto rounded-lg shadow-lg p-8 flex flex-col gap-4 items-center">
            <h2 className="text-2xl font-semibold text-center mb-4">Add or Edit User</h2>
            <div className="flex flex-col gap-4 w-full max-w-md items-center">
                {USER_FIELDS.filter(f => !(f.hideIf && f.hideIf(user))).map(field => {
                    if (field.type === "input") {
                        return (
                            <div key={field.key} className="w-full flex flex-row items-center gap-2">
                                <Label className="w-1/3 text-right pr-2">{field.label}</Label>
                                <Input
                                    type={field.key === "password" ? "password" : "text"}
                                    value={String(user[field.key as keyof ApplicationUser] ?? "")}
                                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => handleFieldChange(field.key as keyof ApplicationUser, e.target.value)}
                                    className="w-2/3 bg-white"
                                />
                            </div>
                        );
                    }
                    if (field.type === "checkbox") {
                        return (
                            <div key={field.key} className="w-full flex flex-row items-center justify-center gap-2">
                                <input
                                    type="checkbox"
                                    checked={!!user[field.key as keyof ApplicationUser]}
                                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => handleFieldChange(field.key as keyof ApplicationUser, e.target.checked)}
                                    className="mr-2"
                                />
                                <Label className="w-fit">{field.label}</Label>
                            </div>
                        );
                    }
                    if (field.type === "dropdown") {
                        const options = field.options ? field.options() : [];
                        return (
                            <div key={field.key} className="w-full flex flex-row items-center gap-2">
                                <Label className="w-1/3 text-right pr-2">{field.label}</Label>
                                <Dropdown
                                    value={String(user[field.key as keyof ApplicationUser] ?? "")}
                                    options={options}
                                    onChange={(e: React.ChangeEvent<HTMLSelectElement>) => handleFieldChange(field.key as keyof ApplicationUser, e.target.value)}
                                    className="min-w=[150px] h-fit bg-white"
                                />
                            </div>
                        );
                    }
                    return null;
                })}
            </div>
            <div className="flex flex-row gap-4 mt-6 justify-center w-full">
                <Button variant="primary" type="button" onClick={props.onClickSave}>Save</Button>
                <Button variant="secondary" type="button" onClick={props.onClose}>Cancel</Button>
            </div>
        </div>
    );
});

export default SingleUserModal;
