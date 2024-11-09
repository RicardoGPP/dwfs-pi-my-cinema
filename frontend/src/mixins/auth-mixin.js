import { jwtDecode } from 'jwt-decode';
import UserService from '@/services/user-service';

export default {
    data() {
        return {
            user: null
        }
    },
    methods: {
        getAuthTokenKey() {
            return 'token';
        },
        getAuthToken() {
            return localStorage.getItem(this.getAuthTokenKey());
        },
        setAuthToken(token) {
            localStorage.setItem(this.getAuthTokenKey(), token);
        },
        clearAuthToken() {
            localStorage.removeItem(this.getAuthTokenKey());
        },
        getUserNameKey() {
            return 'username';
        },
        getUserName() {
            return localStorage.getItem(this.getUserNameKey());
        },
        setUserName(username) {
            localStorage.setItem(this.getUserNameKey(), username);
        },
        clearUserName() {
            localStorage.removeItem(this.getUserNameKey());
        },
        async doLogin(email, password) {
            const credentials = { email, password };
            const token = await UserService.doLogin(credentials);
            this.setAuthToken(token);
            this.refreshPage();
        },
        doLogout() {
            this.clearAuthToken();
            this.clearUserName();
            this.refreshPage();
        },
        refreshPage() {
            window.location.reload();
        },
        refreshAuthData() {
            this.user = null;

            const token = this.getAuthToken();

            if (!token) {
                return;
            }

            let { id, name, email, admin } = jwtDecode(token);

            let username = this.getUserName();

            if (!username) {
                username = name;
            }

            this.user = { id, name: username, email, admin };
        }
    },
    created() {
        this.refreshAuthData();
    }
}