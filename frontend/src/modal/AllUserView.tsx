import React, {useState} from "react";
import AGGridTable from "./../components/AGGridTable";
import {AllCommunityModule, ModuleRegistry, SelectionChangedEvent} from 'ag-grid-community';
import {fetchAllUsers} from "../utils/api";
import Snackbar from "./../components/Snackbar";
import LoadingSpinner from "./../components/LoadingSpinner";
import Button from "./../components/Button";
import {getColDefFromResult, getRowDataFromData} from "../utils/agGridUtils";
import axios from "axios";
import {observer} from "mobx-react-lite";
import {ApplicationUser} from "../utils/ApplicationUser";

ModuleRegistry.registerModules([AllCommunityModule]);


export const AllUserView: React.FC = observer(() => {
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMsg, setSnackbarMsg] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");
    const [currentUser, setCurrentUser] = useState<ApplicationUser | null>(null);
    const [users, setUsers] = React.useState<ApplicationUser[]>([]);

    React.useEffect(() => {
        onLoadUsers();
    }, []);

    const onLoadUsers = async () => {
        setIsLoading(true);
        setError("");
        setSnackbarOpen(false);
        setSnackbarMsg("");
        fetchAllUsers()
            .then((res) => {
                setUsers(res.data);
            })
            .catch((err) => {
                if (axios.isAxiosError(err)) {
                    setError(err.message);
                    setSnackbarOpen(true);
                    setSnackbarMsg("Error Fetching Users: " + err.message);
                } else if (err && typeof err === "object" && "message" in err) {
                    setError(err);
                    setSnackbarOpen(true);
                    setSnackbarMsg("Non API error fetching users: " + err);
                } else {
                    setError("Unknown error");
                    setSnackbarOpen(true);
                    setSnackbarMsg("Non API error fetching users: Unknown error");
                }
            })
            .finally(() => {
                setIsLoading(false);
                setCurrentUser(null)
            });
    };

    // Handler to open modal and load selected user from grid
    const handleSelectionChanged = (event: SelectionChangedEvent) => {
        const selectedRows = event.api.getSelectedRows();
        if (selectedRows.length > 0) {
            setCurrentUser(selectedRows[0]);
        } else {
            setCurrentUser(null);
        }
    };

    const handleEditUser = () => {
        if (currentUser) {
            setSnackbarOpen(true);
            setSnackbarMsg(`Opening edit form for user: ${currentUser.firstName} ${currentUser.lastName}`);
            console.log('Edit user:', currentUser);
        }
    }

    const columnDefs = getColDefFromResult(users);
    const rowDefs = getRowDataFromData(users);
    return (
        <div className={"flex flex-col justify-start min-h-full w-full justify-items-center mt-2"}>
            <div className={"flex flex-col justify-center justify-items-center"}>
                <div
                    className={"flex text-2xl justify-self-center mb-2 flex-row justify-center shadow rounded bg-indigo-100"}>User
                    Details
                </div>
                {isLoading ? (
                    <LoadingSpinner/>
                ) : error ? (
                    <Snackbar open={snackbarOpen} message={snackbarMsg} type={error ? "error" : "success"}
                              onClose={() => setSnackbarOpen(false)}/>
                ) : (
                    <AGGridTable
                        columnDefs={columnDefs}
                        rowData={rowDefs}
                        rowSelection="single"
                        onSelectionChanged={handleSelectionChanged}
                    />
                )}
            </div>
            <div className={"flex flex-row justify-end gap-2 mt-2"}>
                <Button variant={"primary"} type={"submit"} size={"sm"} className={"w-30 mt-2"} onClick={onLoadUsers}>Load
                    Users</Button>
                <Button variant={"primary"} type={"submit"} size={"sm"}
                        className={"w-30  mt-2 !bg-amber-500 hover:!bg-amber-700"}
                        onClick={handleEditUser} disabled={!currentUser}>Edit User</Button></div>
        </div>

    );
});

export default AllUserView;
