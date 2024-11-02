<template>
    <div class="movies-view">
        <h1>
            Cadastro de filmes
        </h1>
        <hr/>
        <LoadingPanel
            v-if="loading"
        />
        <ErrorPanel
            v-else-if="error"
            :message="error"
            :retry="load"
        />
        <div v-else class="content">
            <div class="action">
                <Button
                    label="Atualizar"
                    severity="secondary"
                    @click="load"
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
    </div>
</template>

<script>
import Button from 'primevue/button';
import LoadingPanel from '@/components/loading-panel/LoadingPanel.vue';
import ErrorPanel from '@/components/error-panel/ErrorPanel.vue';
import MovieTable from '@/components/movie-table/MovieTable.vue';
import MovieDialog from '@/components/movie-dialog/MovieDialog.vue';
import MovieOptionDialog from '@/components/movie-option-dialog/MovieOptionDialog.vue';
import MovieService from '@/services/movie-service';

export default {
    components: {
        Button,
        LoadingPanel,
        ErrorPanel,
        MovieTable,
        MovieDialog,
        MovieOptionDialog
    },
    data() {
        return {
            movies: [],
            loading: false,
            error: null,
            dialog: {
                movie: 'MoviesView#MovieDialog',
                movieOption: 'MoviesView#MovieOptionDialog'
            }
        };
    },
    methods: {
        async load() {
            this.movies = [];

            this.loading = true;
            this.error = null;

            try {
                this.movies = await MovieService.getAll();
            } catch (error) {
                this.error = error.message;
            } finally {
                this.loading = false;
            }
        },
        create() {
            const loadMovie = (movieOption) => {
                return MovieService.getMovieByMovieOptionId(movieOption.id);
            };

            const openMovieDialog = (movie) => {
                this.$refs[this.dialog.movie].open('create', movie, onFillMovie);
            };

            const createMovie = (filledMovie) => {
                return MovieService.create(filledMovie);
            };

            const refreshTable = () => {
                return this.load();
            };

            const onFillMovie = (filledMovie) => {
                createMovie(filledMovie)
                    .then(refreshTable);
            };

            const onSelectMovieOption = (movieOption) => {
                loadMovie(movieOption)
                    .then(openMovieDialog);
            };

            this.$refs[this.dialog.movieOption].open(onSelectMovieOption);
        },
        update(movie) {
            const updateMovie = (filledMovie) => {
                return MovieService.update(movie.id, filledMovie);
            };

            const refreshTable = () => {
                this.load();
            };

            const onFillMovie = (filledMovie) => {
                updateMovie(filledMovie)
                    .then(refreshTable);
            };

            this.$refs[this.dialog.movie].open('edit', movie, onFillMovie);
        },
        async remove(movie) {
            try {
                await MovieService.delete(movie.id);
            } catch (error) {
                //TODO: Handle error properly.
                console.error(error.message);
                return;
            }

            this.load();
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