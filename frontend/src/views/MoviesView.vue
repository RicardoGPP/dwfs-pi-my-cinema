<template>
    <div class="movies-view">
        <h1>Filmes</h1>
        
        <Button label="Criar" icon="pi pi-plus" @click="openNewMovieDialog" />
        
        <DataTable :value="movies" paginator rows="10" class="p-datatable-gridlines">
            <Column field="title" header="Título" sortable />
            <Column field="releaseDate" header="Data de Lançamento" sortable />
            <Column field="runtime" header="Duração (min)" sortable />
            <Column header="Ações">
                <template #body="slotProps">
                    <Button label="Editar" icon="pi pi-pencil" class="p-button-text"
                        @click="editMovie(slotProps.data)" />
                    <Button label="Excluir" icon="pi pi-trash" class="p-button-text"
                        @click="confirmDeleteMovie(slotProps.data)" />
                </template>
            </Column>
        </DataTable>

        <!-- Diálogo para criação e edição de filmes -->
        <MovieDialog ref="movieDialog" @save="saveMovie" />

        <!-- Diálogo de confirmação de exclusão -->
        <Dialog header="Confirmar" v-model:visible="deleteDialogVisible" modal>
            <p>Você tem certeza que deseja excluir este filme?</p>
            <Button label="Sim" icon="pi pi-check" @click="deleteMovie" />
            <Button label="Não" icon="pi pi-times" class="p-button-secondary" @click="hideDeleteDialog" />
        </Dialog>
    </div>
</template>

<script>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import movieService from '@/services/movie-service';
import MovieDialog from '@/components/movie-dialog/MovieDialog.vue';

export default {
    components: {
        DataTable,
        Column,
        Button,
        Dialog,
        MovieDialog
    },
    data() {
        return {
            movies: [],
            deleteDialogVisible: false,
            selectedMovie: null
        };
    },
    methods: {
        async loadMovies() {
            this.movies = await movieService.getAll();
        },
        openNewMovieDialog() {
            this.selectedMovie = null;
            this.$refs.movieDialog.openDialog();
        },
        editMovie(movie) {
            this.selectedMovie = movie;
            this.$refs.movieDialog.openDialog(movie);
        },
        async saveMovie(movie) {
            if (this.selectedMovie) {
                await movieService.update(this.selectedMovie.id, movie);
            } else {
                await movieService.create(movie);
            }
            this.loadMovies();
        },
        confirmDeleteMovie(movie) {
            this.selectedMovie = movie;
            this.deleteDialogVisible = true;
        },
        async deleteMovie() {
            await movieService.delete(this.selectedMovie.id);
            this.loadMovies();
            this.deleteDialogVisible = false;
        },
        hideDeleteDialog() {
            this.deleteDialogVisible = false;
        }
    },
    mounted() {
        this.loadMovies();
    }
};
</script>