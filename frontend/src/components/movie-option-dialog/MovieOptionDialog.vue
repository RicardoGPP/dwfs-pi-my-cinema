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
                    <LifecyclePanel :callback="load" :signal="signal">
                        <Listbox
                            v-model="movieOption"
                            :options="movieOptions"
                            optionLabel="title"
                            emptyMessage="Não há opções a exibir"
                            striped
                            listStyle="width: 300px; height: 250px"
                        />
                    </LifecyclePanel>
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
import LifecyclePanel from '@/components/lifecycle-panel/LifecyclePanel.vue';
import MovieService from '@/services/movie-service';
import LifecycleSignalMixin from '@/mixins/lifecycle-signal-mixin';

export default {
    name: 'MovieOptionDialog',
    mixins: [
        LifecycleSignalMixin
    ],
    components: {
        Dialog,
        InputText,
        Listbox,
        Button,
        LifecyclePanel
    },
    data() {
        return {
            visible: false,
            callback: null,
            name: '',
            movieOptions: [],
            movieOption: null
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
                    this.doSignal();
                }
            }, 500);
        },
        reset() {
            this.movieOptions = [];
            this.movieOption = null;
        },
        async load() {
            this.reset();

            const movieOptions = await MovieService.getMovieOptions(this.name);

            this.movieOptions = movieOptions;
            this.movieOption = movieOptions.length === 0 ? null : movieOptions[0];
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