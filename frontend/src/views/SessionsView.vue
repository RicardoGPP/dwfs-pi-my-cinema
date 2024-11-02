<template>
    <div class="sessions-view">
        <h1>
            Cadastro de sessões
        </h1>
        <hr/>
        <LifecyclePanel :callback="load" :signal="signal">
            <div class="content">
                <div class="action">
                    <Button
                        label="Atualizar"
                        severity="secondary"
                        @click="doSignal"
                    />
                    <Button
                        label="Criar"
                        @click="create"
                    />
                </div>
                <SessionTable
                    :sessions="sessions"
                    @update="update"
                    @remove="remove"
                />
                <SessionDialog
                    :ref="dialog.session"
                />
            </div>
        </LifecyclePanel>
    </div>
</template>

<script>
import Button from 'primevue/button';
import LifecyclePanel from '@/components/lifecycle-panel/LifecyclePanel.vue';
import SessionTable from '@/components/session-table/SessionTable.vue';
import SessionDialog from '@/components/session-dialog/SessionDialog.vue';
import SessionService from '@/services/session-service';
import LifecycleSignalMixin from '@/mixins/lifecycle-signal-mixin';

export default {
    name: 'SessionsView',
    mixins: [
        LifecycleSignalMixin
    ],
    components: {
        Button,
        LifecyclePanel,
        SessionTable,
        SessionDialog
    },
    data() {
        return {
            sessions: [],
            dialog: {
                session: 'SessionsView#SessionDialog'
            }
        }
    },
    methods: {
        async load() {
            this.sessions = await SessionService.getAll();
        },
        create() {
            const createSession = (filledSession) => {
                filledSession.movieId = filledSession.movie.id;
                filledSession.locationId = filledSession.location.id;

                delete filledSession.movie;
                delete filledSession.location;

                return SessionService.create(filledSession);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const onFillSession = (filledSession) => {
                createSession(filledSession)
                    .then(refreshTable);
            };

            this.$refs[this.dialog.session].open('create', null, onFillSession);
        },
        update(session) {
            const updateSession = (filledSession) => {
                filledSession.movieId = filledSession.movie.id;
                filledSession.locationId = filledSession.location.id;

                delete filledSession.movie;
                delete filledSession.location;

                return SessionService.update(session.id, filledSession);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const onFillSession = (filledSession) => {
                updateSession(filledSession)
                    .then(refreshTable);
            };

            this.$refs[this.dialog.session].open('edit', session, onFillSession);
        },
        async remove(session) {
            try {
                await SessionService.delete(session.id);
                this.doSignal();
            } catch (error) {
                console.error(error.message);
            }
        }
    },
    mounted() {
        this.load();
    }
};
</script>

<style lang="scss" scoped>
.sessions-view {
    padding: 20px;
}

.content {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.action {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    gap: 5px;
}
</style>