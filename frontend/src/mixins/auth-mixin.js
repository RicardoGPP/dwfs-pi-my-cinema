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
        async doLogin(email, password) {
            const credentials = { email, password };
            const token = await UserService.doLogin(credentials);
            this.setAuthToken(token);
            this.refreshPage();
        },
        doLogout() {
            this.clearAuthToken();
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

            const { id, name, email, admin } = jwtDecode(token);

            this.user = { id, name, email, admin };
        }
    },
    created() {
        this.refreshAuthData();
    }
}