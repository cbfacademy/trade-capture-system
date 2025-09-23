import {makeAutoObservable} from 'mobx';
import {ApplicationUser} from "../utils/ApplicationUser";

class UserStore {
    private _user: ApplicationUser | null = null;
    private _authorization: string | undefined = undefined;
    private _isLoading = false;
    private _error: string | null = null;


    constructor() {
        makeAutoObservable(this);
    }


    get user(): ApplicationUser | null {
        return this._user;
    }

    set user(value: ApplicationUser | null) {
        this._user = value;
    }

    get authorization(): string | undefined {
        return this._authorization;
    }

    set authorization(value: string | undefined) {
        this._authorization = value;
    }

    get isLoading(): boolean {
        return this._isLoading;
    }

    set isLoading(value: boolean) {
        this._isLoading = value;
    }

    get error(): string | null {
        return this._error;
    }

    set error(value: string | null) {
        this._error = value;
    }

}

const userStore = new UserStore();
export default userStore;
