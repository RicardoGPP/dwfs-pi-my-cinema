import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import PrimeVue from 'primevue/config';
import Aura from '@primevue/themes/aura';
import ConfirmationService from 'primevue/confirmationservice';
import LoadingService from '@/plugins/LoadingDialogPlugin';
import ToastService from 'primevue/toastservice';

const app = createApp(App);

app.use(router);

app.use(PrimeVue, {
    theme: {
        preset: Aura
    }
});

app.use(ConfirmationService);

app.use(LoadingService);

app.use(ToastService);

app.mount('#app');