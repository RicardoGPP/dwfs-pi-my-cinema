<template>
    <div class="movie-option-dialog">
        <Dialog
            header="Buscar na fonte de dados"
            v-model:visible="visible"
            :draggable="false"
            modal>
            <div class="content">
                <div class="input">
                    <label for="name">
                        Nome:
                    </label>
                    <InputText
                        id="name"
                        v-model="name"
                        placeholder="Digite o nome do filme"
                        autofocus
                        @input="schedule"
                    />
                </div>
                <div class="result">
                    <LoadingPanel
                        v-if="loading"
                    />
                    <ErrorPanel
                        v-else-if="error"
                        :message="error"
                        :retry="load"
                    />
                    <Listbox
                        v-else
                        v-model="movieOption"
                        :options="movieOptions"
                        optionLabel="title"
                        emptyMessage="Não há opções a exibir"
                        striped
                        listStyle="width: 300px; height: 250px"
                    />
                </div>
            </div>
            <template #footer>
                <Button
                    label="OK"
                    :disabled="!canApply"
                    @click="apply"
                />
                <Button
                    label="Cancelar"
                    severity="secondary"
                    @click="close"
                />
            </template>
        </Dialog>
    </div>
</template>

<script>
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Listbox from 'primevue/listbox';
import Button from 'primevue/button';
import LoadingPanel from '@/components/loading-panel/LoadingPanel.vue';
import ErrorPanel from '@/components/error-panel/ErrorPanel.vue';
import MovieService from '@/services/movie-service';

export default {
    name: 'MovieOptionDialog',
    components: {
        Dialog,
        InputText,
        Listbox,
        Button,
        LoadingPanel,
        ErrorPanel
    },
    data() {
        return {
            visible: false,
            callback: null,
            name: '',
            movieOptions: [],
            movieOption: null,
            controller: null,
            loading: false
        }
    },
    computed: {
        canApply() {
            return this.movieOption !== null;
        }
    },
    methods: {
        open(callback) {
            this.name = '';
            this.movieOptions = [];
            this.movieOption = null;
            this.loading = false;
            this.error = null;
            this.callback = callback;
            this.visible = true;
        },
        close() {
            this.visible = false;
        },
        schedule() {
            const nameAtThisMoment = this.name;
            setTimeout(() => {
                if (nameAtThisMoment === this.name) {
                    this.load();
                }
            }, 500);
        },
        async load() {
            this.movieOptions = [];
            this.movieOption = null;

            this.loading = true;
            this.error = null;

            try {
                this.movieOptions = await MovieService.getMovieOptions(this.name);
            } catch (error) {
                this.error = error.message;
            } finally {
                this.loading = false;
            }
        },
        apply() {
            this.callback(this.movieOption);
            this.close();
        }
    }
}
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    gap: 5px;
    width: 300px;
}

.input {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.result {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 225px;
}
</style>