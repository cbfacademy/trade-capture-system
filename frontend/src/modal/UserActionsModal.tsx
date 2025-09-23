import React from "react";
import Input from "../components/Input";
import Button from "../components/Button";
import api, {createUser} from "../utils/api";
import LoadingSpinner from "../components/LoadingSpinner";
import Snackbar from "../components/Snackbar";
import {observer} from "mobx-react-lite";
import {useQuery} from '@tanstack/react-query';
import staticStore from "../stores/staticStore";
import {SingleUserModal} from "./SingleUserModal";
import {ApplicationUser} from "../utils/ApplicationUser";


function getDefaultUser(): Partial<ApplicationUser> {
    return {
        id: 0,
        firstName: "",
        lastName: "",
        loginId: "",
        password: "",
        active: false,
        userProfile: ""
    };
}

interface UserActionsModalProps {
    user?: ApplicationUser;
    setView?: (view: string, user?: ApplicationUser) => void;
}

export const UserActionsModal: React.FC<UserActionsModalProps> = observer(({ user: initialUser, setView }) => {
    const [userId, setUserId] = React.useState<string>("");
    const [snackBarOpen, setSnackbarOpen] = React.useState<boolean>(false);
    const [snackBarType, setSnackbarType] = React.useState<'success' | 'error'>("success");
    const [user, setUser] = React.useState<ApplicationUser | null>(initialUser || null);
    const [loading, setLoading] = React.useState<boolean>(false);
    const [snackbarMessage, setSnackbarMessage] = React.useState<string>("");

    const {isSuccess, error} = useQuery({
        queryKey: ["staticValues"],
        queryFn: () => staticStore.fetchAllStaticValues(),
        refetchInterval: 30000,
        refetchOnWindowFocus: false,
    });

    React.useEffect(() => {
        if (isSuccess) {
            staticStore.isLoading = false;
        }
        if (error) {
            staticStore.error = (error as Error).message || 'Unknown error';
        }
    }, [isSuccess, error]);

    React.useEffect(() => {
        setUser(initialUser || null);
    }, [initialUser]);

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true)
        try {
            const userResponse = await api.get(`/users/loginId/${userId}`);
            if (userResponse.status === 200) {
                setUser(userResponse.data);
                setSnackbarOpen(true)
                setSnackbarType("success")
                setSnackbarMessage("Successfully fetched user details");
            } else {
                setSnackbarMessage("Error fetching user details: " + userResponse.statusText);
                setSnackbarType("error")
            }
        } catch (error) {
            setSnackbarOpen(true);
            setSnackbarType("error")
            setSnackbarMessage("Error fetching user details: " + (error instanceof Error ? error.message : "Unknown error"));
        } finally {
            setTimeout(() => {
                setSnackbarOpen(false);
                setSnackbarMessage("")
            }, 3000);
            setLoading(false)
            setUserId("")
        }
    };


    const handleSaveUser = async () => {
        setLoading(true);
        try {

            const { id, firstName, lastName, loginId, password, active, userProfile } = user || {};
            let userProfileValue = userProfile;

            if (userProfileValue && typeof userProfileValue === 'object') {
                userProfileValue = userProfileValue.value || userProfileValue.label || "";
            }
            const userPayload = {
                id: id ?? null,
                firstName: firstName ?? "",
                lastName: lastName ?? "",
                loginId: loginId ?? "",
                password: password ?? "",
                active: active ?? false,
                userProfile: userProfileValue ?? ""
            };
            await createUser(userPayload);
            setSnackbarOpen(true);
            setSnackbarMessage('User saved successfully');
            setUser(null);
        } catch (error) {
            setSnackbarOpen(true);
            setSnackbarType("error")
            setSnackbarMessage('Error saving user: ' + (error instanceof Error ? error.message : 'Unknown error'));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={"flex flex-col rounded-lg drop-shadow-2xl mt-0 bg-indigo-50 w-full h-full"}>
            <div className={"flex flex-row items-center justify-center p-4 h-fit w-fit gap-x-2 mb-2 mx-auto"}>
                <Input size={"sm"}
                       type={"search"}
                       required
                       placeholder={"Search by User ID"}
                       key={"user-id"}
                       value={userId}
                       onChange={(e) => setUserId(e.currentTarget.value)}
                       className={"bg-white h-fit w-fit"}/>
                <Button variant={"primary"} type={"button"} size={"sm"} onClick={handleSearch}
                        className={"w-fit h-fit"}>Search</Button>
                <Button variant={"primary"} type={"button"} size={"sm"} onClick={() => setUser(null)}
                        className={"w-fit h-fit !bg-gray-500 hover:!bg-gray-700"}>Clear</Button>
                <Button variant={"primary"} type={"button"} size={"sm"} onClick={() => setUser(getDefaultUser())}
                        className={"w-fit h-fit"}>Add New</Button>
            </div>
            <div>
                {loading ? <LoadingSpinner/> : null}
                {user && !loading && <SingleUserModal
                    isOpen={!!user}
                    user={user}
                    setUser={setUser}
                    onClose={() => setView ? setView('all-users') : setUser(null)}
                    onClickSave={handleSaveUser}
                />}
            </div>
            <Snackbar open={snackBarOpen} message={snackbarMessage} onClose={() => setSnackbarOpen(false)}
                      type={snackBarType}/>
        </div>
    )
})

export default UserActionsModal;
