<template>
    <div class="loading-dialog">
        <Dialog
            v-if="visible"
            v-model:visible="visible"
            :draggable="false"
            :closable="false"
            :closeOnEscape="false"
            :showHeader="false"
            modal>
            <div class="content">
                <LoadingPanel/>
            </div>
        </Dialog>
    </div>
</template>

<script>
import Dialog from 'primevue/dialog';
import LoadingPanel from '@/components/loading-panel/LoadingPanel';
import LoadingDialogService from '@/plugins/LoadingDialogService';

export default {
    name: 'LoadingDialog',
    components: {
        Dialog,
        LoadingPanel
    },
    data() {
        return {
            visible: false,
            promise: null
        }
    },
    methods: {
        register() {
            LoadingDialogService.setOnOpen(() => {
                this.visible = true;
            });

            LoadingDialogService.setOnClose(() => {
                this.visible = false;
            });
        }
    },
    mounted() {
        this.register();
    }
}
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 250px;
    height: 200px;
}
</style>