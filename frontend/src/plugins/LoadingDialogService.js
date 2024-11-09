export default class LoadingDialogService {

    static openCallback = null;
    static closeCallback = null;

    static setOnOpen(callback) {
        this.openCallback = callback;
    }

    static setOnClose(callback) {
        this.closeCallback = callback;
    }

    static open() {
        this.openCallback();
    }

    static close() {
        this.closeCallback();
    }

    static wrap(promise) {
        this.open();
        return (promise || Promise.resolve()).finally(() => this.close());
    }
}