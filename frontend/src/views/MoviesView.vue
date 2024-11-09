<template>
    <div class="movies-view">
        <h1>
            Cadastro de filmes
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
                <MovieTable
                    :movies="movies"
                    @update="update"
                    @remove="remove"
                />
                <MovieDialog
                    :ref="dialog.movie"
                />
                <MovieOptionDialog
                    :ref="dialog.movieOption"
                />
            </div>
        </LifecyclePanel>
    </div>
</template>

<script>
import Button from 'primevue/button';
import LifecyclePanel from '@/components/lifecycle-panel/LifecyclePanel.vue';
import MovieTable from '@/components/movie-table/MovieTable.vue';
import MovieDialog from '@/components/movie-dialog/MovieDialog.vue';
import MovieOptionDialog from '@/components/movie-option-dialog/MovieOptionDialog.vue';
import MovieService from '@/services/movie-service';
import LifecycleSignalMixin from '@/mixins/lifecycle-signal-mixin';

export default {
    name: 'MoviesView',
    mixins: [
        LifecycleSignalMixin
    ],
    components: {
        Button,
        LifecyclePanel,
        MovieTable,
        MovieDialog,
        MovieOptionDialog
    },
    data() {
        return {
            movies: [],
            dialog: {
                movie: 'MoviesView#MovieDialog',
                movieOption: 'MoviesView#MovieOptionDialog'
            }
        }
    },
    methods: {
        async load() {
            this.movies = await MovieService.getAll();
        },
        create() {
            const createMovie = (filledMovie) => {
                return MovieService.create(filledMovie);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'O filme foi criado com sucesso!',
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

            const onAccept = (filledMovie) => {
                this.$loading.wrap(
                    createMovie(filledMovie)
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            const showConfirm = (filledMovie) => {
                this.$confirm.require({
                    header: 'Confirmação',
                    message: 'Deseja realmente criar o filme?',
                    acceptProps: {
                        label: 'Sim'
                    },
                    rejectProps: {
                        label: 'Não',
                        severity: 'secondary'
                    },
                    accept: () => onAccept(filledMovie)
                });
            };

            const loadMovie = (movieOption) => {
                return MovieService.getMovieByMovieOptionId(movieOption.id);
            };

            const openMovieDialog = (movie) => {
                this.$refs[this.dialog.movie].open('create', movie, showConfirm);
            };

            const onSelectMovieOption = (movieOption) => {
                this.$loading.wrap(
                    loadMovie(movieOption)
                        .then(openMovieDialog)
                );
            };

            this.$refs[this.dialog.movieOption].open(onSelectMovieOption);
        },
        update(movie) {
            const updateMovie = (filledMovie) => {
                return MovieService.update(movie.id, filledMovie);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'O filme foi editado com sucesso!',
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

            const onAccept = (filledMovie) => {
                this.$loading.wrap(
                    updateMovie(filledMovie)
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            const showConfirm = (filledMovie) => {
                this.$confirm.require({
                    header: 'Confirmação',
                    message: 'Deseja realmente editar o filme?',
                    acceptProps: {
                        label: 'Sim'
                    },
                    rejectProps: {
                        label: 'Não',
                        severity: 'secondary'
                    },
                    accept: () => onAccept(filledMovie)
                });
            };

            this.$refs[this.dialog.movie].open('edit', movie, showConfirm);
        },
        remove(movie) {
            const removeMovie = () => {
                return MovieService.delete(movie.id);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'O filme foi excluído com sucesso!',
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
                    removeMovie()
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            this.$confirm.require({
                header: 'Confirmação',
                message: 'Deseja realmente excluir o filme?',
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
.movies-view {
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