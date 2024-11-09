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

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'A sessão foi criada com sucesso!',
                    life: 3000
                });
            };

            const showErrorMessage = (error) => {
                this.$toast.add({
                    severity: 'error',
                    summary: 'Erro',
                    detail: error.message,
                    life: 3000
                });
            };

            const onAccept = (filledSession) => {
                this.$loading.wrap(
                    createSession(filledSession)
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            const showConfirm = (filledSession) => {
                this.$confirm.require({
                    header: 'Confirmação',
                    message: 'Deseja realmente criar a sessão?',
                    acceptProps: {
                        label: 'Sim'
                    },
                    rejectProps: {
                        label: 'Não',
                        severity: 'secondary'
                    },
                    accept: () => onAccept(filledSession)
                });
            };

            this.$refs[this.dialog.session].open('create', null, showConfirm);
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

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'A sessão foi editada com sucesso!',
                    life: 3000
                });
            };

            const showErrorMessage = (error) => {
                this.$toast.add({
                    severity: 'error',
                    summary: 'Erro',
                    detail: error.message,
                    life: 3000
                });
            };

            const onAccept = (filledSession) => {
                this.$loading.wrap(
                    updateSession(filledSession)
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            const showConfirm = (filledSession) => {
                this.$confirm.require({
                    header: 'Confirmação',
                    message: 'Deseja realmente editar a sessão?',
                    acceptProps: {
                        label: 'Sim'
                    },
                    rejectProps: {
                        label: 'Não',
                        severity: 'secondary'
                    },
                    accept: () => onAccept(filledSession)
                });
            };

            this.$refs[this.dialog.session].open('edit', session, showConfirm);
        },
        remove(session) {
            const remove = () => {
                return SessionService.delete(session.id);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'A sessão foi excluída com sucesso!',
                    life: 3000
                });
            };

            const showErrorMessage = (error) => {
                this.$toast.add({
                    severity: 'error',
                    summary: 'Erro',
                    detail: error.message,
                    life: 3000
                });
            };

            const onAccept = () => {
                this.$loading.wrap(
                    remove()
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            this.$confirm.require({
                header: 'Confirmação',
                message: 'Deseja realmente excluir a sessão?',
                acceptProps: {
                    label: 'Sim',
                    severity: 'danger'
                },
                rejectProps: {
                    label: 'Não',
                    severity: 'secondary'
                },
                accept: onAccept
            });
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