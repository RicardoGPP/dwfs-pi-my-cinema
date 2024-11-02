<template>
    <LoadingPanel
        v-if="loading"
    />
    <ErrorPanel
        v-else-if="error"
        :message="error"
        :retry="execute"
    />
    <slot v-else/>
</template>

<script>
import LoadingPanel from '@/components/loading-panel/LoadingPanel';
import ErrorPanel from '@/components/error-panel/ErrorPanel';

export default {
    name: 'LifecyclePanel',
    components: {
        LoadingPanel,
        ErrorPanel
    },
    props: {
        callback: {
            type: Function,
            required: true
        },
        signal: {
            type: Number,
            required: false,
            default: 0
        }
    },
    data() {
        return {
            loading: false,
            error: null
        }
    },
    watch: {
        signal(newValue, oldValue) {
            if (newValue > oldValue) {
                this.execute();
            }
        }
    },
    methods: {
        async execute() {
            this.loading = true;
            this.error = null;

            try {
                await this.callback();
            } catch (error) {
                this.error = error.message;
            } finally {
                this.loading = false;
            }
        }
    },
    created() {
        this.execute();
    }
}
</script>