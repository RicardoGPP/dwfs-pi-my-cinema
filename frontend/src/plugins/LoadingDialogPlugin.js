import LoadingDialogService from './LoadingDialogService';

export default {
    install(app) {
        app.config.globalProperties.$loading = {
            wrap(promise) {
                return LoadingDialogService.wrap(promise);
            }
        };
    }
}